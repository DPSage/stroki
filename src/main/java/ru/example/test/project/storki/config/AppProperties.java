package ru.example.test.project.storki.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.Objects;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class AppProperties implements Serializable {
    private static final long serialVersionUID = 1L;

    private String host;
    private Integer port;

    public AppProperties() {}

    public AppProperties(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppProperties that = (AppProperties) o;
        return Objects.equals(host, that.host) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }

    @Override
    public String toString() {
        return "AppProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
