package com.stellargear.cosmicplayer.services.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import uk.co.caprica.vlcj.binding.lib.LibC;
import uk.co.caprica.vlcj.binding.support.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.factory.discovery.strategy.BaseNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.factory.discovery.strategy.LinuxNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.factory.discovery.strategy.OsxNativeDiscoveryStrategy;
import uk.co.caprica.vlcj.factory.discovery.strategy.WindowsNativeDiscoveryStrategy;

public final class VLCBundler {

    private VLCBundler() {}

    public static NativeDiscovery discovery() {
        return new NativeDiscovery(
                new BundledStrategy(),
                new LinuxNativeDiscoveryStrategy(),
                new OsxNativeDiscoveryStrategy(),
                new WindowsNativeDiscoveryStrategy()
        );
    }

    private static class BundledStrategy extends BaseNativeDiscoveryStrategy {

        BundledStrategy() {
            super(
                    new String[] { "libvlc\\.dll", "libvlccore\\.dll" },
                    new String[] { "%s\\plugins" }
            );
        }

        @Override
        public boolean supported() {
            return RuntimeUtil.isWindows();
        }

        @Override
        protected List<String> discoveryDirectories() {
            List<String> dirs = new ArrayList<>();
            try {
                Path source = Path.of(VLCBundler.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
                Path base = Files.isDirectory(source) ? source : source.getParent();
                dirs.add(base.resolve("vlc").toString());
            } catch (Exception ignored) {
            }
            dirs.add(Path.of("vlc").toAbsolutePath().toString());
            return dirs;
        }

        @Override
        protected boolean setPluginPath(String pluginPath) {
            return LibC.INSTANCE._putenv(String.format("%s=%s", PLUGIN_ENV_NAME, pluginPath)) == 0;
        }
    }
}