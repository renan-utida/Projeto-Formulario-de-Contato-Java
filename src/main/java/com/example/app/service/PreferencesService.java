package com.example.app.service;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Serviço expandido para persistir múltiplas preferências. */
public class PreferencesService {

    private final Path file;

    public PreferencesService() {
        this.file = Path.of(System.getProperty("user.home"), ".swing_forms_summary.properties");
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                props.load(in);
            } catch (IOException ignored) {
                System.err.println("Erro ao carregar preferências: " + file);
            }
        }
        return props;
    }

    private void saveProperties(Properties props) {
        try (OutputStream out = Files.newOutputStream(file)) {
            props.store(out, "Swing Forms Summary Preferences - Updated");
        } catch (IOException ignored) {
            System.err.println("Erro ao salvar preferências: " + file);
        }
    }

    // Métodos existentes
    public String loadName() {
        return loadProperties().getProperty("name", "");
    }

    public void saveName(String name) {
        Properties props = loadProperties();
        props.setProperty("name", name == null ? "" : name);
        saveProperties(props);
    }

    // Novos métodos para email
    public String loadLastEmail() {
        return loadProperties().getProperty("lastEmail", "");
    }

    public void saveLastEmail(String email) {
        Properties props = loadProperties();
        props.setProperty("lastEmail", email == null ? "" : email);
        saveProperties(props);
    }

    // Configurações de UI
    public boolean isRememberWindowSize() {
        return Boolean.parseBoolean(loadProperties().getProperty("rememberWindowSize", "true"));
    }

    public void setRememberWindowSize(boolean remember) {
        Properties props = loadProperties();
        props.setProperty("rememberWindowSize", String.valueOf(remember));
        saveProperties(props);
    }

    public Dimension loadWindowSize() {
        Properties props = loadProperties();
        try {
            int width = Integer.parseInt(props.getProperty("windowWidth", "560"));
            int height = Integer.parseInt(props.getProperty("windowHeight", "440"));
            return new Dimension(Math.max(400, width), Math.max(300, height));
        } catch (NumberFormatException e) {
            return new Dimension(560, 440);
        }
    }

    public void saveWindowSize(Dimension size) {
        if (size == null) return;
        Properties props = loadProperties();
        props.setProperty("windowWidth", String.valueOf(size.width));
        props.setProperty("windowHeight", String.valueOf(size.height));
        saveProperties(props);
    }

    // Configuração de histórico
    public int getHistoryMaxSize() {
        Properties props = loadProperties();
        try {
            return Math.max(10, Integer.parseInt(props.getProperty("historyMaxSize", "50")));
        } catch (NumberFormatException e) {
            return 50;
        }
    }

    public void setHistoryMaxSize(int maxSize) {
        Properties props = loadProperties();
        props.setProperty("historyMaxSize", String.valueOf(Math.max(10, maxSize)));
        saveProperties(props);
    }
}