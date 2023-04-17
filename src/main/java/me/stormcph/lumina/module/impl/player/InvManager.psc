method armorSort:
    loop mc.player.getInventory.main:
        if item is ItemBlock:
             loop mc.player.getInventory.main: {
                #if there are already 3 stacks of type block in the inventory, drop the other
        if item is Sword:
            loop mc.player.getInventory.main item: {
                if item is sword:
                    if item is better than currentSword
                        drop currentSword
                        currentSword = item
