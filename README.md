# PayForDeath
 a minecraft spigot plugin that add death penalty feature to the server

SpigotMC release: [PayForDeath | SpigotMC - High Performance Minecraft](https://www.spigotmc.org/resources/payfordeath.94328/)

MCBBS release: [\[机制\]蛋挞君的死亡赎金——让玩家花钱赎回死亡掉落的等级和物品吧！[1.12-1.17]](https://www.mcbbs.net/thread-1213517-1-1.html)

## INTRODUCTION

Many death penalty plugins are either too heavy or outdated, and Kevyn's PayForDeath is a lightweight death penalty plugin base on Vault's money system.

The main feature is very simple: **Players must have enough money to avoid dropping levels and items on death.** It supports different configurations in different world, and the money deduction node can be completely customized.

This provides an elegant way to increase the cost of death, at the same time, it can enrich the economic system of your server, so that the player's money can be used for one more purpose.

![[IMG]](screenshots/screenshot0.png)

![[IMG]](screenshots/screenshot1.png)

![[IMG]](screenshots/screenshot2.png)

![[IMG]](screenshots/screenshot3.png)

![[IMG]](screenshots/screenshot4.png)



## FEATURES

- You can set how much a player needs to pay to keep his levels and items

- Configure kept content: item, level, or both

- Calculate ransom by a Formula you write **[new!]**

- The amount of money can increase with the **player's level** or the **player's balance**

- Customize **messages' text** , send by **action bar** or **console**

- You can configure **clearing** items and exp instead of **dropping** them

- Give a **different configuration** to each world

- **Exempt** (free to keep all) or **ignore** (disable this plugin on) a player **[new!]**

- Messages now support some placeholders **[new!]**

- Read the configuration file for more details (sorry no English-comment version in the jar, you can find a English version below)

  

## INSTALLATION

- Drag to plugins folder

- **Dependencies**:

  - **Vault**
  - Any **economy plugin**
  - Any **permissions management plugin**. 
  - **EssentialX + LuckPerms** is one of the best choices

- Please **fully test before use**. If you encounter any problem, please send me feedback

- If you have used the old version of this plugin before, **please delete the configuration file of the old version manually** before updating

- The default configuration of the plugin is equivalent to turning off all those functions. Please adjust the configuration yourself

- Recommended to install with online rewards, kill mobs rewards or other money-rewards plugins :)

  

## USAGES

- **Commands**:

  ```yaml
  # reload the plugin's config file
  # you can only use it in console
  /pfd reload
  ```

- **Permissions**:

  ```yaml
  # give it to player who you want to save items and levels completely free of charge
  pfd.exempt.[worldName]
  
  # give it to player who you want to disable this plugin's features on
  pfd.ignore.[worldName]
  ```

- **Configuration**:

  ```yaml
  default:
    enable: false
    # formulas
    # supported operations: + - * / ()
    # placeholders you can use:
    # [lv] : the player's level
    # [bal] : the player's balance
    # invalid formula will always return 0.0
    # see the [exampleResourceWorld] below for an example
    deduct-formula: 0
    # -1 to switch off this check
    upper-limit-formula: -1
    keep-inventory: false
    keep-level: false
    clear-instead-of-drop: false
    notice-by-action-bar: false
    notice-by-console: false
    # placeholders you can use in messages:
    # [player] : the player's name
    # [death-world] : the world the player death
    # [respawn-world] : the world the player respawn
    # [ransom] : the money deducted
    # [old-balance] : the player's balance before the deduction
    # [new-balance] : the player's balance after the deduction
    kept-message: §l§byou paid §e$[ransom]§b, inventory and levels kept。now u have §e$[new-balance] §9^_^
    unkept-message: §l§cyou didn't have §e$[ransom]§c, inventory and levels lost in §e[death-world] §9x_x
    exempt-message: §l§eyou have the privilege to keep items and levels for free in §e[death-world] §9^_^
  
  # write your world-specific config below
  # just write the settings you want to override in this world
  # missing settings will read from default
  exampleResourceWorld:
    enable: true
    deduct-formula: 10 + 10 * [lv]
    upper-limit-formula: 10 + 0.5 * [bal]
    keep-inventory: true
    keep-level: true
    notice-by-action-bar: true
  
  exampleMainWorld:
    enable: true
    keep-inventory: true
    keep-level: true
    notice-by-action-bar: true
    kept-message: §l§bFree to keep all things in main world §9^_^
  
  exampleDangerWorld:
    enable: true
    notice-by-action-bar: true
    kept-message: §l§cpay for death is not allowed in this world §9c_c
  ```
  
  

