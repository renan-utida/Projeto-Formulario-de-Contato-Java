# 📧 Contact Form - Sistema de Formulário de Contato em Java Swing

Um sistema completo de formulário de contato desenvolvido em Java Swing com validação em tempo real, histórico de mensagens e persistência de configurações.

## 🚀 Funcionalidades

- ✅ **Formulário de contato** com validação robusta
- 🔍 **Validação em tempo real** com feedback visual
- 📊 **Histórico completo** de mensagens enviadas
- 💾 **Persistência de preferências** do usuário
- 🎨 **Interface moderna** com Nimbus Look & Feel
- ⌨️ **Atalhos de teclado** para melhor usabilidade
- 📱 **Layout responsivo** com redimensionamento automático

## 📋 Pré-requisitos

- **Java 17** ou superior
- **IDE** compatível (IntelliJ IDEA, Eclipse, VS Code)
- **Maven** ou **Gradle** (opcional, para build automatizado)

## 🛠️ Tecnologias Utilizadas

- **Java Swing** - Interface gráfica
- **Java NIO** - Persistência de arquivos
- **GridBagLayout** - Layout avançado
- **CardLayout** - Navegação entre telas
- **Properties API** - Configurações do usuário

## 📁 Estrutura do Projeto

```
src/main/java/com/example/app/
├── 📁 model/
│   ├── Message.java              # Modelo de dados da mensagem
│   └── MessageHistory.java       # Gerenciador do histórico
├── 📁 service/
│   └── PreferencesService.java   # Serviço de persistência
├── 📁 ui/
│   ├── FormPanel.java            # Tela do formulário
│   ├── SummaryPanel.java         # Tela de resumo
│   ├── HistoryPanel.java         # Tela de histórico
│   └── MainFrame.java            # Janela principal
├── 📁 util/
│   ├── IconLoader.java           # Carregador de ícones
│   └── Validation.java           # Utilitários de validação
└── App.java                      # Ponto de entrada
```

## 🎯 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/contact-form-java.git
cd contact-form-java
```

### 2. Compile o projeto
```bash
javac -d build src/main/java/com/example/app/*.java src/main/java/com/example/app/**/*.java
```

### 3. Execute a aplicação
```bash
java -cp build com.example.app.App
```

### Ou usando sua IDE favorita:
- Importe o projeto
- Execute a classe `App.java`

## 🖼️ Interface do Sistema

### Tela Principal - Formulário
- Campos: Nome, E-mail e Mensagem
- Validação em tempo real com feedback visual
- Botão "Enviar" habilitado apenas quando formulário válido

### Tela de Resumo
- Exibe dados da mensagem enviada
- Timestamp da submissão
- Botão para voltar ao formulário

### Tela de Histórico
- Tabela com todas as mensagens enviadas
- Ordenação por data (mais recentes primeiro)
- Opção para limpar histórico completo
- Contador de mensagens no cabeçalho

## ⚙️ Configurações Persistidas

O sistema salva automaticamente:
- **Nome do usuário** (preenchido automaticamente)
- **Último e-mail** utilizado
- **Tamanho da janela** (se habilitado)
- **Tamanho máximo do histórico** (padrão: 50 mensagens)

Arquivo de configuração: `~/.swing_forms_summary.properties`

## ⌨️ Atalhos de Teclado

| Atalho | Ação |
|--------|------|
| `Ctrl + Enter` | Enviar formulário |
| `Ctrl + F` | Ir para formulário |
| `Ctrl + H` | Abrir histórico |
| `Alt + E` | Botão enviar (mnemonic) |
| `Alt + V` | Botão voltar (mnemonic) |
| `Alt + L` | Limpar histórico (mnemonic) |

## 🔧 Melhorias Implementadas

Este projeto passou por um processo de refatoração e melhoria, implementando as seguintes funcionalidades avançadas:

### 1. **Validação em Tempo Real** 
- **InputVerifier** personalizado para campos Nome e E-mail
- **Feedback visual** com bordas vermelhas para campos inválidos
- **Mensagens de erro específicas** abaixo de cada campo
- **Botão enviar inteligente** - habilitado apenas quando todos os campos são válidos
- **Validação instantânea** ao perder foco do campo

### 2. **Sistema de Histórico de Mensagens**
- **Classe MessageHistory** para gerenciar mensagens enviadas
- **Armazenamento thread-safe** com métodos synchronized
- **Limite configurável** de mensagens (evita uso excessivo de memória)
- **Funcionalidade de busca** por e-mail específico
- **Auto-limpeza** quando excede limite máximo

### 3. **Interface de Histórico Avançada**
- **JTable personalizada** com modelo customizado
- **Ordenação cronológica** (mensagens mais recentes primeiro)
- **Truncamento inteligente** de mensagens longas na visualização
- **Botão de limpeza** com confirmação de segurança
- **Contador dinâmico** de mensagens no cabeçalho

### 4. **Sistema de Configurações Expandido**
- **Persistência robusta** com tratamento de erros de I/O
- **Múltiplas preferências** salvas automaticamente:
  - Nome e último e-mail do usuário
  - Dimensões da janela principal
  - Configurações de histórico
- **Valores padrão inteligentes** para todas as configurações
- **Carregamento automático** na inicialização

### 5. **Interface Principal Aprimorada**
- **Menu de navegação** com atalhos de teclado
- **Três telas integradas** via CardLayout
- **Persistência automática** de estado da janela
- **Gestão de recursos** melhorada
- **Sistema de eventos** robusto entre componentes

## 🎨 Recursos de UI/UX

- **Nimbus Look & Feel** aplicado automaticamente
- **Banner customizado** com gradiente e anti-aliasing
- **Ícones escaláveis** carregados dinamicamente
- **Tooltips informativos** em botões importantes
- **Layout responsivo** que se adapta ao redimensionamento

## 📊 Validações Implementadas

### Campo Nome
- Não pode estar vazio ou apenas espaços
- Validação em tempo real ao perder foco

### Campo E-mail
- Formato válido usando regex: `^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$`
- Validação instantânea com feedback visual

### Campo Mensagem
- Não pode estar vazio ou apenas espaços
- Validação ao perder foco da área de texto

## 🔒 Tratamento de Erros

- **Carregamento de preferências**: Valores padrão em caso de erro
- **Salvamento de configurações**: Log de erros sem interromper execução
- **Validação de campos**: Prevenção de submissão com dados inválidos
- **Carregamento de ícones**: Funcionamento normal mesmo sem ícones

## 📝 Arquivos de Configuração

### Localização
```
~/.swing_forms_summary.properties
```

### Exemplo de conteúdo
```properties
name=João Silva
lastEmail=joao.silva@email.com
rememberWindowSize=true
windowWidth=720
windowHeight=580
historyMaxSize=50
```

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Sugestões de Melhorias Futuras

- [ ] **Exportação de dados** para CSV/JSON
- [ ] **Tema escuro/claro** configurável
- [ ] **Validação de e-mail** com verificação de domínio
- [ ] **Backup automático** do histórico
- [ ] **Filtros de busca** no histórico
- [ ] **Integração com APIs** de e-mail
- [ ] **Localização** (i18n) para múltiplos idiomas
- [ ] **Drag & Drop** para anexos
- [ ] **Auto-salvamento** de rascunhos
- [ ] **Templates** de mensagens predefinidos

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 👨‍💻 Autor

**Renan Dias Utida** - RM 558540
- GitHub: [@renan-utida](https://github.com/renan-utida)
- LinkedIn: [Renan Dias Utida](https://www.linkedin.com/in/renan-dias-utida-1b1228225/)
- Email: renandiutida@gmail.com
- Professor Orientador FIAP: Salatiel Marinho

## 🙏 Agradecimentos

- Comunidade Java por manter o Swing atualizado
- Professor Salatiel por ter recomendado e nos orientado a realizar esse projeto
- FIAP pela excelente estrutura
- Desenvolvedores que contribuíram com feedback
- Inspiração em sistemas modernos de formulários web

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela no repositório!**

## 🔍 Tags

`java` `swing` `gui` `desktop-app` `form` `validation` `contact-form` `java-swing` `desktop` `ui` `ux` `persistence`
