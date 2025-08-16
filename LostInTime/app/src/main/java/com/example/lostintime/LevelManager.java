package com.example.lostintime;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    
    public void loadLevel(int level, GameView gameView) {
        List<GameObject> objects = new ArrayList<>();
        List<GameObject> traps = new ArrayList<>();
        Portal portal = null;
        
        switch (level) {
            case 1:
                portal = loadLevel1(objects, traps);
                break;
            case 2:
                portal = loadLevel2(objects, traps);
                break;
            case 3:
                portal = loadLevel3(objects, traps);
                break;
            default:
                portal = loadLevel1(objects, traps);
                break;
        }
        
        gameView.setLevelData(objects, traps, portal);
    }
    
    private Portal loadLevel1(List<GameObject> objects, List<GameObject> traps) {
        // Level 1: Basic movement and portal
        // Ground platform
        objects.add(new Platform(0, 800, 1200, 100));
        
        // Some small platforms to jump on
        objects.add(new Platform(300, 650, 100, 20));
        objects.add(new Platform(500, 550, 100, 20));
        objects.add(new Platform(700, 450, 100, 20));
        
        // Portal at the end
        return new Portal(1000, 700);
    }
    
    private Portal loadLevel2(List<GameObject> objects, List<GameObject> traps) {
        // Level 2: Introduce Pause Time with moving traps
        // Ground platform
        objects.add(new Platform(0, 800, 1200, 100));
        
        // Moving platforms
        objects.add(new MovingPlatform(200, 600, 100, 20, 200, 600, 400, 600));
        objects.add(new MovingPlatform(600, 500, 100, 20, 600, 500, 800, 500));
        
        // Moving traps
        traps.add(new MovingTrap(400, 750, 50, 50, 400, 750, 600, 750));
        traps.add(new MovingTrap(800, 650, 50, 50, 800, 650, 1000, 650));
        
        // Portal at the end
        return new Portal(1000, 700);
    }
    
    private Portal loadLevel3(List<GameObject> objects, List<GameObject> traps) {
        // Level 3: Combine Pause + Reverse with complex obstacles
        // Ground platform
        objects.add(new Platform(0, 800, 1200, 100));
        
        // Complex moving platforms
        objects.add(new MovingPlatform(150, 700, 80, 20, 150, 700, 350, 700));
        objects.add(new MovingPlatform(450, 600, 80, 20, 450, 600, 650, 600));
        objects.add(new MovingPlatform(750, 500, 80, 20, 750, 500, 950, 500));
        
        // Multiple moving traps
        traps.add(new MovingTrap(300, 750, 40, 40, 300, 750, 500, 750));
        traps.add(new MovingTrap(600, 650, 40, 40, 600, 650, 800, 650));
        traps.add(new MovingTrap(900, 550, 40, 40, 900, 550, 1100, 550));
        
        // Static traps
        traps.add(new Trap(200, 750, 30, 30));
        traps.add(new Trap(700, 750, 30, 30));
        
        // Portal at the end
        return new Portal(1000, 700);
    }
}