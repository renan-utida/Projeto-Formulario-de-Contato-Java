# Swing Forms + Summary

Aplicativo Swing com duas telas usando `CardLayout`:

- **Tela 1 (Form)**: nome, e-mail, mensagem; botão **Enviar** com ícone; **validação** e **limpeza**.
- **Tela 2 (Resumo)**: mostra último envio com ícones (user/mail).
- **Preferências**: lembra o nome do usuário com `java.util.Properties` (em `~/.swing_forms_summary.properties`).
- **Extras**: Look & Feel **Nimbus**, banner pintado (AWT), atalho **Ctrl+Enter** para enviar.

## Requisitos
- Java 17+
- Maven 3.8+

## Executar
```bash
mvn -q exec:java
# ou
mvn clean package
java -jar target/swing-forms-summary-1.0.0.jar
```

## Estrutura
```
com/example/app/App.java
com/example/app/ui/MainFrame.java
com/example/app/ui/FormPanel.java
com/example/app/ui/SummaryPanel.java
com/example/app/model/Message.java
com/example/app/service/PreferencesService.java
com/example/app/util/IconLoader.java
com/example/app/util/Validation.java
resources/icons/{app.png, user.png, mail.png, send.png}
```

## Pontos para refatoração e melhorias (sinalizados no código com TODO)
- Propor melhorias de layout, validação de campos, etc;
- Alterar/validar estrutura do projeto;