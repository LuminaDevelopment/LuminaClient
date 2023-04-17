method autoArmor: {
    var[] currentArmor = mc.player.getInventory.armor
    var[] newArmor = var[4]

    loop mc.player.getInventory.main item:{
        if item is armorItem: {
            var score = score(item)
            if item.localizedname is "item.helmet": {
                if score > score(currentArmor[0]) {
                    newArmor[0] = item
                }
            }
            if item.localizedname is "item.chestplate": {
                if score > score(currentArmor[1]) {
                    newArmor[1] = item
                }
            }
            if item.localizedname is "item.leggings": {
                if score > score(currentArmor[2]) {
                    newArmor[2] = item
                }
            }
            if item.localizedname is "item.boots": {
                if score > score(currentArmor[3]) {
                    newArmor[3] = item
                }
            }
        }
    }
}

method score(item): {
    var score
    if item is leather {
        score = 1;
    }
    if item is gold {
        score = 2;
    }
    if item is chain {
        score = 3;
    }
    if item is iron {
        score = 4;
    }
    if item is diamond {
        score = 5;
    }
    if item is netherite {
        score = 6;
    }

    if item has protection {
        score += protectionLevel
    }
}