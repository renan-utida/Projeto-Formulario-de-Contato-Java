package com.example.app.ui;

import com.example.app.model.Message;
import com.example.app.model.MessageHistory;
import com.example.app.service.PreferencesService;
import com.example.app.util.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** Janela principal expandida com histórico e configurações. */
public class MainFrame extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private FormPanel formPanel;
    private SummaryPanel summaryPanel;
    private HistoryPanel historyPanel;
    private final PreferencesService prefs = new PreferencesService();
    private final MessageHistory messageHistory;

    // Menu
    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menuView = new JMenu("Exibir");
    private final JMenuItem menuHistory = new JMenuItem("Histórico");
    private final JMenuItem menuForm = new JMenuItem("Formulário");

    public MainFrame() {
        super("Contato - Sistema Aprimorado");

        // Inicializa histórico
        messageHistory = new MessageHistory(prefs.getHistoryMaxSize());

        setupWindow();
        setupMenus();
        setupPanels();
        setupListeners();

        // Carrega configurações
        loadPreferences();

        cards.show(root, "form");
        pack();
    }

    private void setupWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(560, 440));
        setLocationRelativeTo(null);

        // Ícone da aplicação
        ImageIcon appIcon = IconLoader.load("/icons/app.png", 32);
        if (appIcon != null) setIconImage(appIcon.getImage());
    }

    private void setupMenus() {
        // Configurar menu
        menuHistory.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        menuForm.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));

        menuView.add(menuForm);
        menuView.add(menuHistory);
        menuBar.add(menuView);

        setJMenuBar(menuBar);

        // Ações do menu
        menuForm.addActionListener(e -> cards.show(root, "form"));
        menuHistory.addActionListener(e -> {
            historyPanel.refreshHistory();
            cards.show(root, "history");
        });
    }

    private void setupPanels() {
        // Banner superior
        JPanel banner = new BannerPanel();
        banner.setPreferredSize(new Dimension(560, 90));

        // Instancia painéis
        formPanel = new FormPanel();
        summaryPanel = new SummaryPanel();
        historyPanel = new HistoryPanel(messageHistory);

        // Adiciona ao CardLayout
        root.add(formPanel, "form");
        root.add(summaryPanel, "summary");
        root.add(historyPanel, "history");

        // Layout principal
        setLayout(new BorderLayout(0, 0));
        add(banner, BorderLayout.NORTH);
        add(root, BorderLayout.CENTER);
    }

    private void setupListeners() {
        // Listeners dos painéis
        formPanel.setOnSubmit(this::handleSubmit);
        summaryPanel.setOnBack(e -> cards.show(root, "form"));
        historyPanel.setOnBack(e -> cards.show(root, "form"));

        // Pré-carrega dados do usuário
        formPanel.prefillName(prefs.loadName());

        // Listener para salvar tamanho da janela
        if (prefs.isRememberWindowSize()) {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (getExtendedState() == JFrame.NORMAL) {
                        prefs.saveWindowSize(getSize());
                    }
                }
            });
        }

        // Listener para limpar recursos ao fechar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (prefs.isRememberWindowSize()) {
                    prefs.saveWindowSize(getSize());
                }
            }
        });
    }

    private void loadPreferences() {
        // Carrega tamanho da janela
        if (prefs.isRememberWindowSize()) {
            Dimension savedSize = prefs.loadWindowSize();
            setSize(savedSize);
        }
    }

    private void handleSubmit(Message msg) {
        // Salva preferências
        prefs.saveName(msg.getName());
        prefs.saveLastEmail(msg.getEmail());

        // Adiciona ao histórico
        messageHistory.addMessage(msg);

        // Atualiza painel de resumo
        summaryPanel.updateSummary(msg);

        // Navega para resumo
        cards.show(root, "summary");
    }

    /** Banner customizado mantido igual. */
    static class BannerPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            Color base = new Color(30, 136, 229);
            Color dark = base.darker();
            GradientPaint gp = new GradientPaint(0, 0, base, w, h, dark);
            g2.setPaint(gp);
            g2.fillRoundRect(10, 10, w - 20, h - 20, 24, 24);

            g2.setColor(Color.WHITE);
            g2.setFont(getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString("Formulário de Contato", 26, 48);
            g2.dispose();
        }
    }
}