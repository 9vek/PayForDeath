# PayForDeath
 a minecraft spigot plugin that add death penalty feature to the server

SpigotMC release: [PayForDeath | SpigotMC - High Performance Minecraft](https://www.spigotmc.org/resources/payfordeath.94328/)

MCBBS release: [\[机制\]蛋挞君的死亡赎金——让玩家花钱赎回死亡掉落的等级和物品吧！[1.12-1.17]](https://www.mcbbs.net/thread-1213517-1-1.html)

## **[ INTRODUCTION ]**

Many death penalty plugins are either too heavy or outdated, and Kevyn's PayForDeath is a lightweight death penalty plugin base on Vault's money system.

The main feature is very simple: **Players must have enough money to avoid dropping levels and items on death.** It supports different configurations in different world, and can be completely customized.

This provides an elegant way to increase the cost of death, at the same time, it can enrich the economic system of your server, so that the player's money can be used for one more purpose.

![[IMG]](screenshots/screenshot0.png)

![[IMG]](screenshots/screenshot1.png)

![[IMG]](screenshots/screenshot2.png)

![[IMG]](screenshots/screenshot3.png)

![[IMG]](screenshots/screenshot4.png)



## **[ FEATURES ]**

- You can set how much a player needs to pay to keep his levels and items

- Configure kept content: item, level, or both

- Calculate ransom by a **fixed amount**, or a **percent**, or a **combination of the two**

- The amount of money can increase with the **player's level**

- Customize **messages' text** , send by **action bar** or **console**

- You can configure **clearing** items and exp instead of **dropping** them

- Switch **on / off** or give a **different configuration** to each world

- **Exempt** (free to keep all) or **ignore** (disable this plugin on) a player **[new!]**

- Read the configuration file for more details (sorry no English-comment version in the jar, you can find a English version below)

  

## **[ INSTALLATION ]**

- Need **Vault** + one any **economy provider plugin** + one any **permissions management plugin**. **EssentialX + LuckPerms** is one of the best choices

- Please fully test before use. If you encounter any problem, please send me feedback

- If you have used the old version of this plugin before, please delete the configuration file of the old version manually before updating

- The default configuration of the plugin is equivalent to turning off all those functions. Please adjust the configuration yourself

- Recommended to install with online rewards, kill mobs rewards or other money-rewards plugins :)

  

## **[ USAGES ]**

- Command:

  ```
  # reload the plugin's config file
  # you can only use it in console
  /pfd reload
  ```

- Permissions:

  ```
  # give it to player who you want to save items and levels completely free of charge
  pfd.exempt.[worldName]
  
  # give it to player who you want to disable this plugin's features on
  pfd.ignore.[worldName]
  ```

- Configuration:

  ```yaml
  # Enable worlds of this plugin
  enabled-worlds:
    - world
    - anotherWorld
    
  # world-specific configuration sections:
  # See the default configuration section below to know what settings can be written here. 
  # Settings under each world name will override the default in that world.
  
  # Some examples
  # Free death in the main world
  exampleMainWorld:
    keep-inventory: true
    keep-level: true
    notice-by-action-bar: true
    kept-message: §l§9Keeping items and levels is free in the main world :D
  
  
  # danger world
  exampleDangerWorld:
    # Because those keep-settings in default already have the value false
    # You can just not write them here
    notice-by-action-bar: true
    # Why isn't this an unkept-message?
    # Because players' unkept is not cause by their money lack
    # It's beacause the setting. All keep-settings are false (inherited from default)
    kept-message: §l§cThis world does not allow anyone pay to redeem items and levels T _ T
  
    
  # Fixed amount deduction in this world, and clear instead of drop
  exampleWorld1:
    deduct-by-amount: true
    base-amount: 100
    level-increase-amount: 10
    max-amount: -1
    clear-instead-of-drop: true
    keep-inventory: true
    keep-level: true
    notice-by-action-bar: true
  
  
  # Proportional deduction in this world
  exampleWorld2:
    deduct-by-percent: true
    base-percent: 10
    level-increase-percent: 1
    max-percent: 100
    keep-inventory: true
    keep-level: true
    notice-by-action-bar: true
    
    
  # Default configuration section
  # If the world you configured is missing an setting, the default one will be read
  default:
    # Note: you can turn on both deduction modes
    # So you can combine some more customized ways to deduct money
    # But this may make the amount of money deducted very difficult to calculate, 
    # If you both set the maximum deduction amount and the maximum deduction percent 
    # PayForDeath will take the lower one as the upper deduction limit.
    # If both modes of deduction are turned off, it is equivalent to
    # keep items and levels free of charge
    
    # deduct by fixed amount
    # turn the first to true to let the following three take effect
    deduct-by-amount: false
    # Basic amount
    base-amount: 0
    # Level increase amount
    level-increase-amount: 0
    # Max amount, - 1 to turn off this check
    max-amount: -1
  
    # deduct by percent (of player's balance)
    # turn the first to true to let the following three take effect
    deduct-by-percent: false
    # Base percent
    base-percent: 0
    # Level increase percent
    level-increase-percent: 0
    # Max percent, - 1 to turn off this check
    max-percent: -1
    
    # If the player has enough money, what to keep?
    # All off by default, which is equivalent to disabling the plugin
    keep-inventory: false
    keep-level: false
    
    # If the player does NOT has enough money
    # clear all items and exp without drop
    clear-instead-of-drop: false
    
    # Notification methods
    # Action bar notification:
    notice-by-action-bar: false
    # console chat notification
    notice-by-message: false
    
    # default messages
    kept-message: §l§bYou have paid §e%s §bcoins，items and levels kept ^_^
    unkept-message: §l§cYou didn't have enough §e%s §ccoins，items and levels lost x _ x
    exempt-message: §l§eYou have the privilege to keep items and levels for free in this world ^_^
  ```

  

