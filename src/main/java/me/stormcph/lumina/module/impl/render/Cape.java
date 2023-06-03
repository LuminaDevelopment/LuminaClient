package me.stormcph.lumina.module.impl.render;

import me.stormcph.lumina.cape.CapeManager;
import me.stormcph.lumina.event.EventTarget;
import me.stormcph.lumina.event.impl.EventUpdate;
import me.stormcph.lumina.module.Category;
import me.stormcph.lumina.module.Module;
import me.stormcph.lumina.setting.impl.ModeSetting;
import me.stormcph.lumina.utils.misc.GithubRetriever;

public class Cape extends Module {

    private static final ModeSetting cape = new ModeSetting("cape", "TestCape", "TestCape", "TestCape2", "Lightning");

    /**
     * @see me.stormcph.lumina.mixins.AbstractClientPlayerEntityMixin
     */
    public Cape() {
        super("Cape", "Forces a custom cape", Category.RENDER);
        addSettings(cape);
        this.toggle();
    }

    @Override
    public void onEnable() {
        // Whenever the module is enabled, we retrieve the latest capes from the github repo
        CapeManager.players.clear();
        GithubRetriever retriever = new GithubRetriever();
        retriever.retrieveCustomData();
        retriever.retrieve();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        getCurrentCape().update();
        CapeManager.players.forEach((s, cape1) -> cape1.update());
    }

    public static me.stormcph.lumina.cape.Cape getCurrentCape() {

        switch (cape.getMode().toLowerCase()) {
            case "testcape" -> {
                return CapeManager.getCape("TestCape");
            }
            case "testcape2" -> {
                return CapeManager.getCape("TestCape2");
            }
            case "lightning" -> {
                return CapeManager.getCape("LightningCape");
            }
        }

        return CapeManager.getCape("TestCape");
    }

    // render code in AbstractClientPlayerEntityMixin.java

}
