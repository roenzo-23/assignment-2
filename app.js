// MindSpark App - app.js
// All data is static/dummy and handled in-browser. No APIs.

// --- Dummy Data ---
const WRITING_PROMPTS = [
  "Describe your perfect creative day.",
  "Invent a new holiday and explain how it's celebrated.",
  "Write a letter to your future self.",
  "If you could have any superpower, what would it be?",
  "Describe a world where everyone has a unique talent.",
  "What inspires you the most and why?",
  "Write a story that starts with: 'It was a spark in the dark...'"
];
const BRAIN_TEASERS = [
  { q: "What has keys but can't open locks?", a: "A piano" },
  { q: "I speak without a mouth and hear without ears. What am I?", a: "An echo" },
  { q: "What comes once in a minute, twice in a moment, but never in a thousand years?", a: "The letter M" },
  { q: "What gets wetter as it dries?", a: "A towel" },
  { q: "What can travel around the world while staying in a corner?", a: "A stamp" }
];
const QUOTES = [
  "Creativity is intelligence having fun. – Albert Einstein",
  "You can’t use up creativity. The more you use, the more you have. – Maya Angelou",
  "The best way to get started is to quit talking and begin doing. – Walt Disney",
  "Every artist was first an amateur. – Ralph Waldo Emerson",
  "Motivation is what gets you started. Habit is what keeps you going. – Jim Ryun"
];
const MOOD_EMOJIS = ["😃", "🙂", "😐", "😕", "😢"];
const MICRO_HABITS = [
  "Drink a full glass of water after waking up.",
  "Write down one thing you're grateful for.",
  "Take a 5-minute mindful breathing break.",
  "Do 10 push-ups or stretches.",
  "Read one page of a book.",
  "Compliment someone or yourself.",
  "Go for a 5-minute walk.",
  "Organize one small thing on your desk.",
  "Write a single sentence in your journal.",
  "Plan tomorrow's top priority.",
  "Smile at yourself in the mirror.",
  "Unplug from screens for 10 minutes.",
  "Tidy up your workspace.",
  "Send a thank you message to someone.",
  "List 3 things you did well today."
];

// --- State ---
const state = {
  user: null,
  showQuote: true,
  soundOn: true,
  streak: 0,
  lastActive: null,
  mood: null,
  writingDone: false,
  puzzleDone: false,
  journal: [],
  todayPrompt: null,
  todayTeaser: null,
  writingInput: '',
  puzzleInput: '',
};

// --- LocalStorage Keys ---
const LS_KEYS = {
  user: 'ms_user',
  showQuote: 'ms_show_quote',
  soundOn: 'ms_sound_on',
  streak: 'ms_streak',
  lastActive: 'ms_last_active',
  mood: 'ms_mood',
  writingDone: 'ms_writing_done',
  puzzleDone: 'ms_puzzle_done',
  journal: 'ms_journal',
  todayPrompt: 'ms_today_prompt',
  todayTeaser: 'ms_today_teaser',
  writingInput: 'ms_writing_input',
  puzzleInput: 'ms_puzzle_input',
};

// --- Utility Functions ---
function $(sel, root=document) { return root.querySelector(sel); }
function $$(sel, root=document) { return Array.from(root.querySelectorAll(sel)); }
function saveState() {
  localStorage.setItem(LS_KEYS.user, state.user);
  localStorage.setItem(LS_KEYS.showQuote, state.showQuote);
  localStorage.setItem(LS_KEYS.soundOn, state.soundOn);
  localStorage.setItem(LS_KEYS.streak, state.streak);
  localStorage.setItem(LS_KEYS.lastActive, state.lastActive);
  localStorage.setItem(LS_KEYS.mood, state.mood);
  localStorage.setItem(LS_KEYS.writingDone, state.writingDone);
  localStorage.setItem(LS_KEYS.puzzleDone, state.puzzleDone);
  localStorage.setItem(LS_KEYS.journal, JSON.stringify(state.journal));
  localStorage.setItem(LS_KEYS.todayPrompt, JSON.stringify(state.todayPrompt));
  localStorage.setItem(LS_KEYS.todayTeaser, JSON.stringify(state.todayTeaser));
  localStorage.setItem(LS_KEYS.writingInput, state.writingInput);
  localStorage.setItem(LS_KEYS.puzzleInput, state.puzzleInput);
}
function loadState() {
  state.user = localStorage.getItem(LS_KEYS.user) || null;
  state.showQuote = localStorage.getItem(LS_KEYS.showQuote) !== 'false';
  state.soundOn = localStorage.getItem(LS_KEYS.soundOn) !== 'false';
  state.streak = parseInt(localStorage.getItem(LS_KEYS.streak) || '0');
  state.lastActive = localStorage.getItem(LS_KEYS.lastActive) || null;
  state.mood = localStorage.getItem(LS_KEYS.mood) || null;
  state.writingDone = localStorage.getItem(LS_KEYS.writingDone) === 'true';
  state.puzzleDone = localStorage.getItem(LS_KEYS.puzzleDone) === 'true';
  try {
    state.journal = JSON.parse(localStorage.getItem(LS_KEYS.journal)) || [];
  } catch { state.journal = []; }
  try {
    state.todayPrompt = JSON.parse(localStorage.getItem(LS_KEYS.todayPrompt));
  } catch { state.todayPrompt = null; }
  try {
    state.todayTeaser = JSON.parse(localStorage.getItem(LS_KEYS.todayTeaser));
  } catch { state.todayTeaser = null; }
  state.writingInput = localStorage.getItem(LS_KEYS.writingInput) || '';
  state.puzzleInput = localStorage.getItem(LS_KEYS.puzzleInput) || '';
}
function playSound() {
  if (state.soundOn) {
    const audio = $("#sound-complete");
    if (audio) { audio.currentTime = 0; audio.play(); }
  }
}
function todayStr() {
  return new Date().toISOString().slice(0,10);
}
function resetDaily() {
  if (state.lastActive !== todayStr()) {
    state.writingDone = false;
    state.puzzleDone = false;
    state.mood = null;
    state.lastActive = todayStr();
    state.todayPrompt = randomFrom(WRITING_PROMPTS);
    state.todayTeaser = randomFrom(BRAIN_TEASERS);
    state.writingInput = '';
    state.puzzleInput = '';
    saveState();
  }
}

// --- App Init ---
function askName() {
  showModal({
    title: "Welcome to MindSpark!",
    content: `<p>What's your name?</p><input id='name-input' type='text' maxlength='16' style='width:100%;padding:0.5rem;font-size:1.1rem;border-radius:8px;border:none;margin-top:1rem;'>`,
    confirmText: "Let's Go!",
    onConfirm: () => {
      const val = $("#name-input").value.trim();
      if (val) {
        state.user = val;
        saveState();
        closeModal();
        renderApp();
      }
    },
    hideCancel: true
  });
  setTimeout(() => { $("#name-input").focus(); }, 200);
}

function renderApp() {
  loadState();
  resetDaily();
  renderNav();
  renderSection('dashboard');
}

// --- Navigation ---
function renderNav() {
  const navLinks = $$('.nav-links li');
  navLinks.forEach(li => {
    li.onclick = () => {
      navLinks.forEach(l => l.classList.remove('active'));
      li.classList.add('active');
      renderSection(li.dataset.section);
    };
  });
}

function renderSection(section) {
  const main = $('#main-content');
  main.innerHTML = '';
  let el;
  switch(section) {
    case 'dashboard': el = dashboardSection(); break;
    case 'journal': el = journalSection(); break;
    case 'progress': el = progressSection(); break;
    case 'settings': el = settingsSection(); break;
    default: el = dashboardSection();
  }
  el.classList.add('section');
  main.appendChild(el);
  setTimeout(() => el.classList.add('visible'), 30);
}

// --- Dashboard ---
function dashboardSection() {
  const sec = document.createElement('section');
  sec.className = 'container';
  // Prompt Writing Card
  let promptCard = document.createElement('div');
  promptCard.className = 'card';
  promptCard.innerHTML = `
    <h3>✍️ Prompt Writing</h3>
    <button class="button" id="generate-prompt-btn">Generate Prompt</button>
    <div id="prompt-output" style="margin-top:1.2rem;"></div>
    <div id="prompt-input-block" style="margin-top:1.2rem;display:none;">
      <textarea id="prompt-input" rows="3" maxlength="400" placeholder="Write your response..." style="width:100%;padding:0.7rem;font-size:1rem;border-radius:8px;border:none;resize:vertical;"></textarea>
      <button class="button" id="submit-prompt-btn" style="margin-top:0.7rem;">Submit</button>
      <div id="prompt-user-output" style="margin-top:1rem;"></div>
    </div>
  `;
  // Brain Teaser Card
  let teaserCard = document.createElement('div');
  teaserCard.className = 'card';
  teaserCard.innerHTML = `
    <h3>🧠 Brain Teaser</h3>
    <button class="button" id="generate-teaser-btn">Generate Teaser</button>
    <div id="teaser-output" style="margin-top:1.2rem;"></div>
    <div id="teaser-input-block" style="margin-top:1.2rem;display:none;">
      <input id="teaser-input" type="text" maxlength="100" placeholder="Your answer..." style="width:100%;padding:0.7rem;font-size:1rem;border-radius:8px;border:none;" />
      <button class="button" id="submit-teaser-btn" style="margin-top:0.7rem;">Check</button>
      <div id="teaser-user-output" style="margin-top:1rem;"></div>
    </div>
  `;
  // Micro Habit Challenge Section
  let microHabitSection = document.createElement('div');
  microHabitSection.className = 'micro-habit-section';
  microHabitSection.innerHTML = `
    <div class="micro-habit-title">Micro Habit Challenge</div>
    <div class="micro-habit-desc">Build momentum with tiny daily actions! Start a 7-day micro habit challenge to boost your creativity, health, and productivity. Each day, complete a simple, positive action. Mark each day as you go!</div>
    <button class="micro-habit-btn" id="start-micro-habit-btn">Start Challenge</button>
    <div class="micro-habit-timeline" id="micro-habit-timeline"></div>
  `;
  // Cards Row
  let cardsRow = document.createElement('div');
  cardsRow.className = 'cards-row';
  cardsRow.append(promptCard, teaserCard);
  sec.appendChild(cardsRow);
  sec.appendChild(microHabitSection);

  // Prompt Writing Logic
  const promptBtn = promptCard.querySelector('#generate-prompt-btn');
  const promptOutput = promptCard.querySelector('#prompt-output');
  const promptInputBlock = promptCard.querySelector('#prompt-input-block');
  const promptInput = promptCard.querySelector('#prompt-input');
  const promptSubmitBtn = promptCard.querySelector('#submit-prompt-btn');
  const promptUserOutput = promptCard.querySelector('#prompt-user-output');
  let currentPrompt = '';
  promptBtn.onclick = () => {
    currentPrompt = WRITING_PROMPTS[Math.floor(Math.random()*WRITING_PROMPTS.length)];
    promptOutput.innerHTML = `<div class='fade-in' style='font-size:1.1rem;color:var(--color-primary);'>${currentPrompt}</div>`;
    promptInputBlock.style.display = 'block';
    promptInput.value = '';
    promptUserOutput.innerHTML = '';
  };
  promptSubmitBtn.onclick = () => {
    if (promptInput.value.trim().length > 0) {
      promptUserOutput.innerHTML = `<div class='fade-in' style='color:var(--color-primary);'>${promptInput.value.trim()}</div>`;
      promptInput.value = '';
    } else {
      promptInput.focus();
      promptInput.style.boxShadow = '0 0 0 2px var(--color-primary)';
      setTimeout(() => promptInput.style.boxShadow = '', 800);
    }
  };

  // Brain Teaser Logic
  const teaserBtn = teaserCard.querySelector('#generate-teaser-btn');
  const teaserOutput = teaserCard.querySelector('#teaser-output');
  const teaserInputBlock = teaserCard.querySelector('#teaser-input-block');
  const teaserInput = teaserCard.querySelector('#teaser-input');
  const teaserSubmitBtn = teaserCard.querySelector('#submit-teaser-btn');
  const teaserUserOutput = teaserCard.querySelector('#teaser-user-output');
  let currentTeaser = null;
  teaserBtn.onclick = () => {
    currentTeaser = BRAIN_TEASERS[Math.floor(Math.random()*BRAIN_TEASERS.length)];
    teaserOutput.innerHTML = `<div class='fade-in' style='font-size:1.1rem;color:var(--color-primary);'>${currentTeaser.q}</div>`;
    teaserInputBlock.style.display = 'block';
    teaserInput.value = '';
    teaserUserOutput.innerHTML = '';
  };
  teaserSubmitBtn.onclick = () => {
    if (teaserInput.value.trim().length > 0) {
      const userAns = teaserInput.value.trim().toLowerCase();
      const correct = currentTeaser && userAns === currentTeaser.a.toLowerCase();
      if (correct) {
        teaserUserOutput.innerHTML = `<div class='fade-in' style='color:var(--color-primary);'>Correct! 🎉 (${currentTeaser.a})</div>`;
      } else {
        teaserUserOutput.innerHTML = `<div class='fade-in' style='color:#e50914;'>Try again!</div>`;
      }
    } else {
      teaserInput.focus();
      teaserInput.style.boxShadow = '0 0 0 2px var(--color-primary)';
      setTimeout(() => teaserInput.style.boxShadow = '', 800);
    }
  };

  // Micro Habit Challenge Logic
  const startHabitBtn = microHabitSection.querySelector('#start-micro-habit-btn');
  const habitTimeline = microHabitSection.querySelector('#micro-habit-timeline');
  let challenge = [];
  let completedDays = Array(7).fill(false);
  startHabitBtn.onclick = () => {
    // Generate 7 unique random habits
    challenge = [];
    let used = new Set();
    while (challenge.length < 7) {
      let idx = Math.floor(Math.random()*MICRO_HABITS.length);
      if (!used.has(idx)) {
        used.add(idx);
        challenge.push(MICRO_HABITS[idx]);
      }
    }
    completedDays = Array(7).fill(false);
    renderHabitTimeline();
  };
  function renderHabitTimeline() {
    habitTimeline.innerHTML = '';
    challenge.forEach((habit, i) => {
      const day = document.createElement('div');
      day.className = 'micro-habit-day';
      day.style.setProperty('--delay', `${i*0.08}s`);
      if (completedDays[i]) day.classList.add('completed');
      day.innerHTML = `<div style="font-weight:600;">Day ${i+1}</div><div style="margin:0.5rem 0 0.2rem 0;">${habit}</div><button class="micro-habit-check${completedDays[i] ? ' completed' : ''}" title="Mark as done">${completedDays[i] ? '✔️' : '○'}</button>`;
      const checkBtn = day.querySelector('.micro-habit-check');
      checkBtn.onclick = () => {
        completedDays[i] = !completedDays[i];
        renderHabitTimeline();
      };
      habitTimeline.appendChild(day);
    });
  }

  return sec;
}
function randomFrom(arr) {
  return arr[Math.floor(Math.random()*arr.length)];
}
function animateQuote() {
  if (!state.showQuote) return;
  const quote = randomFrom(QUOTES);
  const el = $('#quote-block');
  el.innerHTML = '';
  let i = 0;
  function type() {
    if (i < quote.length) {
      el.innerHTML += quote[i++];
      setTimeout(type, 24);
    }
  }
  type();
}
function checkStreak() {
  if (state.writingDone && state.puzzleDone) {
    // If both done today, increment streak
    if (state.lastActive === todayStr()) {
      state.streak = (state.streak || 0) + 1;
      state.lastActive = todayStr();
    }
  }
}

// --- Journal Section ---
function journalSection() {
  const sec = document.createElement('section');
  sec.innerHTML = `
    <h2>Journal</h2>
    <form id="journal-form" autocomplete="off" style="margin:1.5rem 0;">
      <textarea id="journal-input" rows="3" maxlength="400" placeholder="Write your thoughts..." style="width:100%;padding:1rem;font-size:1.1rem;border-radius:12px;border:none;"></textarea>
      <button type="submit" style="margin-top:0.7rem;">Add Entry</button>
    </form>
    <div class="timeline" id="journal-timeline"></div>
  `;
  $('#journal-form', sec).onsubmit = e => {
    e.preventDefault();
    const val = $('#journal-input', sec).value.trim();
    if (val) {
      state.journal.unshift({ text: val, date: new Date().toLocaleString() });
      saveState();
      renderSection('journal');
    }
  };
  renderJournalTimeline(sec);
  return sec;
}
function renderJournalTimeline(sec) {
  const timeline = $('#journal-timeline', sec);
  timeline.innerHTML = '';
  state.journal.forEach((entry, idx) => {
    const div = document.createElement('div');
    div.className = 'timeline-entry';
    div.innerHTML = `
      <div class="entry-date">${entry.date}</div>
      <div class="entry-text">${entry.text}</div>
      <div class="entry-actions">
        <button class="edit-btn" title="Edit">✏️</button>
        <button class="delete-btn" title="Delete">🗑️</button>
      </div>
    `;
    // Edit
    $('.edit-btn', div).onclick = () => {
      showModal({
        title: 'Edit Entry',
        content: `<textarea id='edit-input' rows='4' style='width:100%;padding:1rem;font-size:1.1rem;border-radius:12px;border:none;'>${entry.text}</textarea>`,
        confirmText: 'Save',
        onConfirm: () => {
          const val = $('#edit-input').value.trim();
          if (val) {
            state.journal[idx].text = val;
            saveState();
            closeModal();
            renderSection('journal');
          }
        }
      });
    };
    // Delete
    $('.delete-btn', div).onclick = () => {
      showModal({
        title: 'Delete Entry?',
        content: '<p>Are you sure you want to delete this entry?</p>',
        confirmText: 'Delete',
        onConfirm: () => {
          state.journal.splice(idx,1);
          saveState();
          closeModal();
          renderSection('journal');
        }
      });
    };
    timeline.appendChild(div);
  });
}

// --- Progress Section ---
function progressSection() {
  const sec = document.createElement('section');
  sec.innerHTML = `
    <h2>Progress</h2>
    <div style="margin:2rem 0;display:flex;flex-wrap:wrap;gap:2rem;justify-content:center;">
      <div class="card" style="min-width:220px;">
        <h3>🔥 Streak</h3>
        <div class="progress-bar"><div class="progress-bar-inner" style="width:${Math.min(state.streak, 7)*14.28}%"></div></div>
        <div style="font-size:1.2rem;">${state.streak} day${state.streak===1?'':'s'} in a row!</div>
      </div>
      <div class="card" style="min-width:220px;">
        <h3>📝 Journal Entries</h3>
        <div style="font-size:2.2rem;">${state.journal.length}</div>
      </div>
      <div class="card" style="min-width:220px;">
        <h3>📈 Completion</h3>
        <div class="progress-bar"><div class="progress-bar-inner" style="width:${completionRate()}%"></div></div>
        <div style="font-size:1.1rem;">${completionRate()}% tasks done</div>
      </div>
    </div>
    <div id="progress-charts" style="margin-top:2rem;"></div>
  `;
  setTimeout(() => renderCharts(), 200);
  return sec;
}
function completionRate() {
  // Dummy: 2 tasks per day, count days with both done
  let days = state.streak || 1;
  let done = days * 2;
  let completed = (state.writingDone?1:0) + (state.puzzleDone?1:0);
  return Math.round((completed/done)*100);
}
function renderCharts() {
  // Bonus: simple animated bar chart for streak
  const el = $('#progress-charts');
  if (!el) return;
  el.innerHTML = `<div style="width:100%;max-width:400px;margin:0 auto;">
    <div style="height:32px;display:flex;align-items:end;gap:4px;">
      ${Array.from({length:7}).map((_,i)=>`<div style="flex:1;background:${i<state.streak?'var(--color-primary)':'#333'};height:${20+Math.random()*60}px;border-radius:6px;transition:all 0.7s;"></div>`).join('')}
    </div>
    <div style="text-align:center;color:var(--color-muted);font-size:0.9rem;margin-top:0.5rem;">Last 7 days</div>
  </div>`;
}

// --- Settings Section ---
function settingsSection() {
  const sec = document.createElement('section');
  sec.innerHTML = `
    <h2>Settings</h2>
    <div style="margin:2rem 0;display:flex;flex-direction:column;gap:2rem;max-width:400px;">
      <div class="card" style="padding:1.2rem 1rem;">
        <label style="display:flex;align-items:center;gap:1rem;">
          <input type="checkbox" id="toggle-quote" ${state.showQuote?'checked':''} />
          Show Motivational Quote
        </label>
      </div>
      <div class="card" style="padding:1.2rem 1rem;">
        <label style="display:flex;align-items:center;gap:1rem;">
          <input type="checkbox" id="toggle-sound" ${state.soundOn?'checked':''} />
          Sound Effects
        </label>
      </div>
      <div class="card" style="padding:1.2rem 1rem;">
        <button id="clear-data-btn" style="width:100%;background:var(--color-primary);color:#fff;font-size:1.1rem;padding:0.7rem 0;border-radius:8px;border:none;">Clear All Data</button>
      </div>
    </div>
  `;
  $('#toggle-quote', sec).onchange = e => {
    state.showQuote = e.target.checked;
    saveState();
  };
  $('#toggle-sound', sec).onchange = e => {
    state.soundOn = e.target.checked;
    saveState();
  };
  $('#clear-data-btn', sec).onclick = () => {
    showModal({
      title: 'Clear All Data?',
      content: '<p>This will erase all your MindSpark progress. Are you sure?</p>',
      confirmText: 'Clear',
      onConfirm: () => {
        localStorage.clear();
        closeModal();
        location.reload();
      }
    });
  };
  return sec;
}

// --- Modal System ---
function showModal({title, content, confirmText, onConfirm, hideCancel}) {
  const root = $('#modal-root');
  root.innerHTML = `<div class="modal">
    <button class="close-btn" title="Close">×</button>
    <h3 style="margin-top:0;">${title||''}</h3>
    <div style="margin:1.2rem 0;">${content||''}</div>
    <div style="display:flex;gap:1rem;justify-content:flex-end;">
      ${!hideCancel?'<button class="cancel-btn">Cancel</button>':''}
      <button class="confirm-btn" style="background:var(--color-primary);color:#fff;padding:0.5rem 1.2rem;border-radius:8px;border:none;">${confirmText||'OK'}</button>
    </div>
  </div>`;
  root.classList.add('active');
  $('.close-btn', root).onclick = closeModal;
  if (!hideCancel) $('.cancel-btn', root).onclick = closeModal;
  $('.confirm-btn', root).onclick = () => { if (onConfirm) onConfirm(); };
}
function closeModal() {
  const root = $('#modal-root');
  root.classList.remove('active');
  setTimeout(() => { root.innerHTML = ''; }, 400);
}

// --- Scroll Animations ---
function setupScrollAnimations() {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible');
      }
    });
  }, { threshold: 0.2 });
  $$('.card, .timeline-entry').forEach(el => observer.observe(el));
}

document.addEventListener('DOMContentLoaded', () => {
  loadState();
  if (!state.user) {
    askName();
  } else {
    renderApp();
  }
  // Animate background
  animateBackground();
});

function animateBackground() {
  // Bonus: animated particles
  const bg = document.getElementById('background-animation');
  if (!bg) return;
  let particles = [];
  for (let i=0; i<18; ++i) {
    const p = document.createElement('div');
    p.style.position = 'absolute';
    p.style.width = p.style.height = (16+Math.random()*24)+"px";
    p.style.borderRadius = '50%';
    p.style.background = 'rgba(229,9,20,0.08)';
    p.style.left = (Math.random()*100)+"vw";
    p.style.top = (Math.random()*100)+"vh";
    p.style.filter = 'blur(1.5px)';
    p.style.transition = 'all 2.5s cubic-bezier(.4,0,.2,1)';
    bg.appendChild(p);
    particles.push(p);
  }
  setInterval(() => {
    particles.forEach(p => {
      p.style.left = (Math.random()*100)+"vw";
      p.style.top = (Math.random()*100)+"vh";
      p.style.width = p.style.height = (16+Math.random()*24)+"px";
    });
  }, 3200);
}