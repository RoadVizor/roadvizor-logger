package school.sptech;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
public class Logs {


    String nivel;
    Double[] listaAleatorios = new Double[3];

    String Horario(){
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataFormatada = timestamp.format(formato);

        return dataFormatada;
    }



    void Login(Boolean in){
        Thread login = new Thread(() -> {

            String mensagem;
            nivel = "INFO";
            mensagem = "[%s] %s Fazendo consulta no banco de dados...";
            System.out.println(mensagem.formatted(nivel,Horario()));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){
                nivel = "ERROR";
                System.out.println("[%s] %s Consulta interrompida".formatted(nivel,Horario()));
            }
            if (in == false) {
                nivel = "ERRO";
                mensagem = "[%s] %s erro de autentificação um dos campos Email, Senha e Codigo de ativação retorna null".formatted(nivel,Horario());
                System.out.println(mensagem);
                return;

            }
            nivel = "INFO";
            mensagem = "[%s] %s login/cadastro realizado com sucesso! redirecionando...".formatted(nivel,Horario());
            System.out.println(mensagem);
        } );
        login.start();

    }


    void capturaDados(Boolean captura){
        Thread dados = new Thread(() -> {
        String saida;


        nivel = "INFO";
        String take = """
                [%s] %s Começando a captura do uso dos componentes...
                """;
        System.out.println(take.formatted(nivel,Horario()));

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            nivel = "FATAL";
            System.out.println("[%s] %s Captura interrompida".formatted(nivel,Horario()));
        }
            if (captura == false){
                nivel =  "DEBUG";
                saida = """
                        [%s] %s Variavel 'NucleoCPU8' is undefined...""";
                System.out.println(saida.formatted(nivel,Horario()));

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e){
                    nivel = "FATAL";
                    System.out.println("[%s] %s Captura interrompida".formatted(nivel,Horario()));
                }

                nivel = "FATAL";
                saida = """
                        [%s] %s Erro ao capturar dados do servidor 'PADR-S501'...""";
                System.out.println(saida.formatted(nivel,Horario()));
                    return;
                 }
            nivel = "INFO";
            saida = """
                    [%s] %s Dados de servidor 'PADR-S501' capturados com sucesso...""";
            System.out.println(saida.formatted(nivel,Horario()));
        });
        dados.start();
    }

    void tratamento(Boolean trat){
        Thread trato = new Thread(() -> {
        String out;

        Double CPU = listaAleatorios[0];
        Double RAM = listaAleatorios[1];
        Double Disco = listaAleatorios[2];




        nivel = "INFO";
        String begin = """
             [%s] %s começando tratamento do arquivo 'relatorio_servidor.csv'""";
            System.out.println(begin.formatted(nivel,Horario()));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            nivel = "FATAL";
            System.out.println("[%s] %s Captura interrompida".formatted(nivel,Horario()));
        }

            if (trat == false ){
                nivel = "FATAL";
                 out = """
                       [%s] %s não e possivel ler arquivo 'relatorio_servidor.csv'""";
                System.out.println(out.formatted(nivel,Horario()));
                return;

            }
            nivel = "INFO";
            out = """
                    [%s] %s Filtrando dados do pedagio: 'Imigrantes KM45 '""";
        System.out.println(out.formatted(nivel,Horario()));


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e){
            nivel = "FATAL";
            System.out.println("[%s] %s Captura interrompida".formatted(nivel,Horario()));
        }

        if (CPU > 80.0){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e){
                nivel = "FATAL";
                System.out.println("[%s] %s Captura interrompida".formatted(nivel,Horario()));
            }
            nivel = "WARN";
            System.out.println("""
            
        [%s]
        %s 
        ===============================
        CPU acima de %.2f...      
        abrindo chamado no JIRA
        ===============================
        
                        """.formatted(nivel,Horario(),CPU));

        }

            if (RAM > 80.0){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    nivel = "FATAL";
                    System.out.println("""
                [%s] 
                %s 
                Captura interrompida""".formatted(nivel,Horario()));
                }
                nivel = "WARN";
                System.out.println("""
                        
                        [%s] 
                        %s 
                        ===============================
                        RAM acima de %.2f... 
                        abrindo chamado no JIRA
                        ===============================
                        
                        """.formatted(nivel,Horario(),RAM));

            } if (Disco > 80.0){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    nivel = "FATAL";
                    System.out.println("""
                            [%s] 
                            %s 
                            Captura interrompida""".formatted(nivel,Horario()));
                }
                nivel = "WARN";
                System.out.println("""
                        
                        [%s] 
                        %s 
                        ===============================
                        Disco acima de %.2f... 
                        abrindo chamado no JIRA
                        ===============================
                        
                        """.formatted(nivel,Horario(),Disco));
            }

        System.out.println("""
                        
                [%s] 
                %s 
                ===============================
                CPU: %.2f 
                RAM: %.2f 
                Disco: %.2f 
                ===============================
                Dados filtrados com sucesso encaminhando para dashboard...
                
                        """.formatted(nivel,Horario(),CPU,RAM,Disco));
        });
    trato.start();
    }
}
