package com.example.app.ui;

import com.example.app.model.Message;
import com.example.app.util.IconLoader;
import com.example.app.util.Validation;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/** Tela 1: Formulário. */
public class FormPanel extends JPanel {

    private final JTextField txtName = new JTextField(20);
    private final JTextField txtEmail = new JTextField(20);
    private final JTextArea  txtBody = new JTextArea(6, 20);
    private final JButton btnSend   = new JButton("Enviar");

    private Consumer<Message> onSubmit = m -> {};

    public FormPanel() {
        super(new BorderLayout(10, 10));

        // Centro: formulário com GridBagLayout
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lblName = new JLabel("Nome:");
        JLabel lblEmail = new JLabel("E-mail:");
        JLabel lblMsg = new JLabel("Mensagem:");

        txtBody.setLineWrap(true);
        txtBody.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(txtBody);

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0; form.add(lblName, gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.weightx=1; form.add(txtName, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0; form.add(lblEmail, gbc);
        gbc.gridx=1; gbc.gridy=1; gbc.weightx=1; form.add(txtEmail, gbc);

        gbc.gridx=0; gbc.gridy=2; gbc.weightx=0; form.add(lblMsg, gbc);
        gbc.gridx=1; gbc.gridy=2; gbc.weightx=1; form.add(scroll, gbc);

        // Rodapé: botão enviar com ícone, tooltip, mnemonic e atalho Ctrl+Enter
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ImageIcon sendIcon = IconLoader.load("/icons/send.png", 18);
        if (sendIcon != null) btnSend.setIcon(sendIcon);
        btnSend.setToolTipText("Envia o formulário (Ctrl+Enter)");
        btnSend.setMnemonic('E');
        footer.add(btnSend);

        // Ação do botão
        btnSend.addActionListener(e -> trySubmit());

        // Atalho Ctrl+Enter
        getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke("ctrl ENTER"), "send");
        getActionMap().put("send", new AbstractAction() {
            @Override public void actionPerformed(java.awt.event.ActionEvent e) { trySubmit(); }
        });

        add(form, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setOnSubmit(java.util.function.Consumer<Message> onSubmit) {
        this.onSubmit = onSubmit != null ? onSubmit : m -> {};
    }

    public void prefillName(String name) {
        txtName.setText(name == null ? "" : name);
    }

    private void trySubmit() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String body = txtBody.getText().trim();

        if (!Validation.notBlank(name) || !Validation.isEmail(email) || !Validation.notBlank(body)) {
            JOptionPane.showMessageDialog(this,
                    "Preencha Nome, E-mail válido e Mensagem.",
                    "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Message m = new Message(name, email, body);
        onSubmit.accept(m);

        // Limpeza dos campos
        txtBody.setText("");
        // Mantém nome e e-mail (útil em apps reais). Para limpar tudo, descomente:
        // txtName.setText(""); txtEmail.setText("");
    }

    // TODO: aplicar InputVerifier por campo e destacar visualmente campos inválidos
    // TODO: extrair strings fixas para uma classe de mensagens (i18n)
}
