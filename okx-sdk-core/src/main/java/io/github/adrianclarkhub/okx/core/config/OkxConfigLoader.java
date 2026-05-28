package io.github.adrianclarkhub.okx.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * OKX 配置加载器。
 *
 * <p>按业界常见优先级加载配置并合并为唯一的 {@link OkxConfig}：</p>
 * <ol>
 *     <li>代码默认值</li>
 *     <li>系统属性 {@value #CONFIG_FILE_PROPERTY} 或环境变量 {@value #CONFIG_FILE_ENV} 指定的文件</li>
 *     <li>环境变量（{@code OKX_*})</li>
 * </ol>
 */
public final class OkxConfigLoader {

    /**
     * 指定外部配置文件路径的系统属性名。
     */
    public static final String CONFIG_FILE_PROPERTY = "okx.config.file";

    /**
     * 指定外部配置文件路径的环境变量名。
     */
    public static final String CONFIG_FILE_ENV = "OKX_CONFIG_FILE";

    private OkxConfigLoader() {
    }

    /**
     * 加载 SDK 配置。
     *
     * @return 合并后的 SDK 根配置
     */
    public static OkxConfig load() {
        OkxConfig config = new OkxConfig();
        mergeFile(config, loadExternalProperties());
        OkxConfigBinder.applyEnvironmentVariables(config);
        config.normalize();
        return config;
    }

    /**
     * 从指定 classpath 资源加载配置。
     *
     * @param classpathResource classpath 资源路径
     * @return SDK 根配置
     */
    public static OkxConfig loadFromClasspath(String classpathResource) {
        OkxConfig config = new OkxConfig();
        mergeFile(config, loadClasspathProperties(classpathResource));
        OkxConfigBinder.applyEnvironmentVariables(config);
        config.normalize();
        return config;
    }

    /**
     * 从指定文件路径加载配置。
     *
     * @param path 配置文件路径
     * @return SDK 根配置
     */
    public static OkxConfig loadFromPath(Path path) {
        OkxConfig config = new OkxConfig();
        mergeFile(config, loadPathProperties(path));
        OkxConfigBinder.applyEnvironmentVariables(config);
        config.normalize();
        return config;
    }

    private static void mergeFile(OkxConfig config, Properties properties) {
        OkxConfigBinder.apply(properties, config);
    }

    private static Properties loadClasspathProperties(String classpathResource) {
        Properties properties = new Properties();
        if (classpathResource == null || classpathResource.isEmpty()) {
            return properties;
        }
        ClassLoader classLoader = OkxConfigLoader.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(classpathResource)) {
            if (inputStream != null) {
                properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load OKX config from classpath: " + classpathResource, e);
        }
        return properties;
    }

    private static Properties loadExternalProperties() {
        String configFile = System.getProperty(CONFIG_FILE_PROPERTY);
        if (configFile == null || configFile.trim().isEmpty()) {
            configFile = System.getenv(CONFIG_FILE_ENV);
        }
        if (configFile == null || configFile.trim().isEmpty()) {
            return new Properties();
        }
        return loadPathProperties(Paths.get(configFile.trim()));
    }

    private static Properties loadPathProperties(Path path) {
        Properties properties = new Properties();
        if (path == null || !Files.isRegularFile(path)) {
            return properties;
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load OKX config from file: " + path, e);
        }
        return properties;
    }
}
