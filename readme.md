# 🖥️ RoadVizor Logger

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

> Aplicação desenvolvida em Java para simular o monitoramento de servidores, manipulação de logs estruturados por níveis de severidade e rotinas assíncronas com tratamento de datas.

---

## 🎯 Objetivo

O **RoadVizor Logger** foi projetado para simular o ciclo de vida e a rotina de auditoria de um servidor. Durante a execução, a aplicação executa etapas operacionais simuladas e registra logs detalhados, disparando alertas automáticos em cenários críticos.

### Etapas Simuladas
- 🔐 Verificação de permissões para provisionamento do servidor
- 🏗️ Criação e inicialização do servidor
- 📡 Captura de dados e métricas em tempo real
- 📑 Leitura, filtragem e tratamento de arquivos CSV
- 📊 Validação dos níveis de consumo de **CPU**, **RAM** e **Disco**
- 🎫 Integração lógica para abertura automática de chamados no **JIRA** ao atingir limites

---

## 🛠️ Tecnologias Utilizadas

- **Java** 
- **`java.time`**: `LocalDateTime`, `LocalDate`, `DateTimeFormatter`
- **`java.lang`**: `Thread`, `Thread.sleep()`
- **`java.util`**: `Scanner`, `ThreadLocalRandom`

---

## 📂 Estrutura do Projeto

```text
roadvizor-logger/
│
├── Data.java          # Operações e manipulação com LocalDate e LocalDateTime
├── Formatacao.java    # Padronização de exibição de datas com DateTimeFormatter
├── Hora.java          # Parsing e geração de timestamps com LocalDateTime
├── Logs.java          # Núcleo da aplicação (geração de mensagens e regras de negócio)
└── Main.java          # Ponto de entrada, entrada do usuário e definição das métricas