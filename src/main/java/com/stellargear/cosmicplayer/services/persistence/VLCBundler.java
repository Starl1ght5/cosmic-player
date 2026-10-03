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

/**
 * Locates the native VLC libraries needed by vlcj.
 *
 * <p>On Windows it first looks for a bundled copy of VLC next to the
 * application (in a {@code vlc} directory), falling back to the system
 * installation if not found. On Linux and macOS the standard system
 * discovery strategies are used.</p>
 *
 * <p>This class cannot be instantiated; use {@link #discovery()}.</p>
 *
 * @author Starl1ght5
 * @since 1.0
 */
public final class VLCBundler {

    private VLCBundler() {}

    /**
     * Creates a {@link NativeDiscovery} configured with the bundled
     * strategy (Windows) and the standard platform strategies.
     *
     * @return a discovery instance ready to be passed to {@code MediaPlayerFactory}
     */
    public static NativeDiscovery discovery() {
        return new NativeDiscovery(
                new BundledStrategy(),
                new LinuxNativeDiscoveryStrategy(),
                new OsxNativeDiscoveryStrategy(),
                new WindowsNativeDiscoveryStrategy()
        );
    }

    /**
     * Discovery strategy that looks for a VLC copy bundled with the application.
     * Only active on Windows.
     */
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
                // Resolve the directory containing the application itself
                Path source = Path.of(VLCBundler.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
                Path base = Files.isDirectory(source) ? source : source.getParent();
                dirs.add(base.resolve("vlc").toString());
            } catch (Exception ignored) {
            }
            // Fallback: vlc directory relative to the working directory
            dirs.add(Path.of("vlc").toAbsolutePath().toString());
            return dirs;
        }

        /**
         * Sets the VLC_PLUGIN_PATH environment variable, required for VLC
         * to find its plugins directory.
         *
         * @param pluginPath the path to the plugins directory
         * @return {@code true} if the variable was set successfully
         */
        @Override
        protected boolean setPluginPath(String pluginPath) {
            return LibC.INSTANCE._putenv(String.format("%s=%s", PLUGIN_ENV_NAME, pluginPath)) == 0;
        }
    }
}