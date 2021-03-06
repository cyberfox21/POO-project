/**
 * *
 * @author Nuno Ferreira
 */

package projeto;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Estatisticas {

    private ArrayList<Pessoa> utilizadores;
    private ArrayList<Sala> salas;
    private ArrayList<Curso> cursos;
    private ArrayList<Exame> exames;

    public Estatisticas(){}

    public Estatisticas(ArrayList<Pessoa> utilizadores,ArrayList<Sala> salas,ArrayList<Curso> cursos,ArrayList<Exame> exames){
        this.utilizadores = utilizadores;
        this.salas = salas;
        this.cursos = cursos;
        this.exames = exames;
    }

    /**
     * Método que apresenta ao utilizador o menu para esta classe
     */

    public void menuEstatisticas(){
        int choice;
        long numero;
        String epoca,disc,curso,toFile = null;
        int nota;
        Scanner sc = new Scanner(System.in);
        Exame exa = new Exame(),temp;
        Aluno aluno = new Aluno(),al = null;
        boolean ctrl = false;
        do {
            System.out.println("MENU DE ESTATÍSTICAS\n1 - Listar exames\n2 - Listar alunos inscritos num exame e classificações obtidas" +
                    "\n3 - Listar exames em que um aluno está inscrito e classificações\n4 - Listar docentes e funcionários associados a um exame" +
                    "\n5 - Listar exames em que um docente ou funcionário está envolvido\n6 - Listar notas de um exame\n0 - voltar ao menu inicial");
            while (true){
                try {
                    choice = Integer.parseInt(sc.nextLine());
                    if(choice< 0 ||choice>6){
                        System.out.println("Número inválido");
                    }
                    else break;
                }catch (NumberFormatException e){
                    System.err.println("Introduza um número entre 1 e 6");
                }
            }
            switch (choice){
                case 1:
                    listarExames();
                    break;
                case 2:
                    listarAlunosExamesNotas();
                    break;
                case 3:
                    listarExamesAlunoNotas();
                    break;
                case 4:
                    listarPessoasExame();
                    break;
                case 5:
                    listarPessoa();
                    break;
                case 6:
                    listarNotas();
                    break;
                case 0:
                    Departamento dep = new Departamento();
                    dep.handleChoice(this.utilizadores,this.cursos,this.exames,this.salas);
                default:
                    System.out.println("Parâmetro não reconhecido");
                    break;
            }
        }while(choice!=0);
    }

    /**
     * Método que permite listar exames
     */
    public void listarExames(){
        String toFile = "";
        toFile +="\nOpção nº1: \n";
        System.out.println();
        for(Exame ex: this.exames){
            System.out.println(ex.toString()+"\n-------------------\n");
            toFile +=ex.toString();
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite listar alunos inscritos num exame e classificações obtidas
     */
    public void listarAlunosExamesNotas(){
        String toFile="",curso,disc,epoca;
        Scanner sc = new Scanner(System.in);
        Exame exa = new Exame();
        Exame temp;
        boolean ctrl;
        toFile +="\nOpção nº2: \n";
        do{
            System.out.println("Nome do curso: ");
            curso = sc.nextLine();
            System.out.println("Nome da disciplina: ");
            disc = sc.nextLine();
            System.out.println("Época: ");
            epoca = sc.nextLine();
            ctrl = exa.inExameList(disc,curso,epoca);
            temp = exa.returnExame(disc,epoca,curso);
        }while(ctrl || temp==null);
        if(temp.getNotas().containsValue(-1)){
            System.out.println("Notas do exame de "+temp.getDisciplina().getNome()+" ainda não foram lançadas");
            for(Map.Entry<Aluno,Integer> entry: temp.getNotas().entrySet()){
                toFile +=entry.getKey().getNome() + "\n";
                System.out.println(entry.getKey().getNome());
            }
        }
        else{
            System.out.println("Alunos associados ao exame: ");
            for(Map.Entry<Aluno,Integer> entry: temp.getNotas().entrySet()){
                toFile+= entry.getKey().getNome() + "\t\t\t" + entry.getValue()+"\n";
                System.out.println(entry.getKey().getNome()+"\t\t\tNota: "+entry.getValue());
            }
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite listar exames em que um aluno está inscrito e classificações
     */
    public void listarExamesAlunoNotas(){
        String toFile="";
        int numero,nota;
        Aluno al;
        Aluno aluno = new Aluno();
        Scanner sc = new Scanner(System.in);
        boolean ctrl = false;
        toFile +="\nOpção nº3: \n";
        System.out.println("Número do aluno: ");
        numero = Integer.parseInt(sc.nextLine());
        al = aluno.parseAluno(utilizadores,numero);
        while(al == null){
            System.out.println("Número do aluno não existe no sistema por favor introduza um número de aluno válido: ");
            numero = Integer.parseInt(sc.nextLine());
            al = aluno.parseAluno(utilizadores,numero);
        }
        for(Exame ex: exames){
            for (Map.Entry<Aluno, Integer> entry : ex.getNotas().entrySet()) {
                if (entry.getKey().getNumeroAluno() == al.getNumeroAluno()) {
                    ctrl = true;
                    nota = entry.getValue();
                    if (nota == -1) {
                        toFile +=ex.toString()+"Nota do exame de "+ex.getDisciplina().getNome()+" do aluno com o numero "+al.getNumeroAluno()+" ainda não foi lançada\n-----------------------\n";
                        System.out.println(ex.toString()+"Nota do exame de "+ex.getDisciplina().getNome()+" ainda não foi lançada\n-----------------------\n");

                    } else {
                        toFile += ex.toString()+"Nota do exame de "+ex.getDisciplina().getNome()+" do aluno com o numero "+al.getNumeroAluno()+"\t\t" + nota + "\n-----------------------\n";
                        System.out.println(ex.toString()+"Nota do exame de "+ex.getDisciplina().getNome()+"\t\t" + nota + "\n-----------------------\n");
                    }
                }
            }
        }
        if(!ctrl){
            System.out.println("Aluno não se encontra inscrito em nenhum exame");
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite listar docentes e funcionários associados a um exame
     */
    public void listarPessoasExame(){
        String toFile="",curso,disc,epoca;
        Scanner sc = new Scanner(System.in);
        boolean ctrl;
        Exame exa = new Exame();
        Exame temp;
        toFile +="\nOpção nº4: \n";
        do{
            System.out.println("Nome do curso: ");
            curso = sc.nextLine();
            System.out.println("Nome da disciplina: ");
            disc = sc.nextLine();
            System.out.println("Época: ");
            epoca = sc.nextLine();
            ctrl = exa.inExameList(disc,curso,epoca);
            temp = exa.returnExame(disc,epoca,curso);
        }while(ctrl || temp==null);
        for(Docente doc: temp.getVigilantes()){
            toFile+=doc.toString()+"\n";
            System.out.println(doc.toString());
        }
        System.out.println("---------------------\nAuxiliares associados ao exame");
        for(NaoDocente ndoc: temp.getFuncionarios()){
            toFile+=ndoc.toString()+"\n";
            System.out.println(ndoc.toString());
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite listar exames em que um docente ou funcionário está envolvido
     */
    public void listarPessoa(){
        String toFile="";
        boolean ctrl = false;
        long numero;
        Scanner sc = new Scanner(System.in);

        toFile +="\nOpção nº5: \n";
        ctrl = false;
        System.out.println("Numero do funcionário: ");
        numero = Long.parseLong(sc.nextLine());
        for(Exame ex: exames){
            if(ex.getDocenteResponsavel().getNumero() == numero){
                ctrl = true;
                toFile+=ex.toString()+"O funcionário "+ex.getDocenteResponsavel().getNome()+" encontra-se inscrito para vigiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString()+"\n";
                System.out.println(ex.toString()+"O funcionário "+ex.getDocenteResponsavel().getNome()+" encontra-se inscrito para vigiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString());
            }
            for(Docente doc: ex.getVigilantes()){
                if(doc.getNumero() == numero){
                    ctrl = true;
                    toFile+= ex.toString()+"O funcionário "+doc.getNome()+" encontra-se inscrito para vigiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString()+"\n";
                    System.out.println(ex.toString()+"O funcionário "+doc.getNome()+" encontra-se inscrito para vigiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString());
                }
            }
            for(NaoDocente ndoc: ex.getFuncionarios()){
                if(ndoc.getNumero() == numero){
                    ctrl = true;
                    toFile +=ex.toString()+"O funcionário "+ndoc.getNome()+" encontra-se inscrito para apoiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString()+"\n";
                    System.out.println(ex.toString()+"O funcionário "+ndoc.getNome()+" encontra-se inscrito para apoiar o exame de "+ex.getDisciplina().getNome()+" em "+ex.getDate().toString());
                }
            }
        }
        if(!ctrl){
            toFile+="Funcionário não se encontra associado a nenhum exame marcado\n";
            System.out.println("Funcionário não se encontra associado a nenhum exame marcado");
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite listar as notas de um exame
     */
    public void listarNotas(){
        String toFile="",curso,disc,epoca;
        Scanner sc = new Scanner(System.in);
        boolean ctrl;
        toFile +="\nOpção nº6: \n";
        Exame exa = new Exame();
        Exame temp;
        do{
            System.out.println("Nome do curso: ");
            curso = sc.nextLine();
            System.out.println("Nome da disciplina: ");
            disc = sc.nextLine();
            System.out.println("Época: ");
            epoca = sc.nextLine();
            ctrl = exa.inExameList(disc,curso,epoca);
            temp = exa.returnExame(disc,epoca,curso);
        }while(ctrl || temp == null);
        if(temp.getNotas().containsValue(-1)){
            toFile += "Notas do exame de "+temp.getDisciplina().getNome()+" ainda não foram lançadas\n";
            System.out.println("Notas do exame de "+temp.getDisciplina().getNome()+" ainda não foram lançadas");
        }
        else{
            System.out.println("Notas do exame de: "+temp.getDisciplina().getNome());
            for(Map.Entry<Aluno,Integer> entry: temp.getNotas().entrySet()){
                toFile += entry.getKey().getNome()+"\t\t\tNota: "+entry.getValue()+"\n";
                System.out.println(entry.getKey().getNome()+"\t\t\tNota: "+entry.getValue());
            }
        }
        saveToFile(toFile);
    }

    /**
     * Método que permite guardar a informação no ficheiro de texto das estatísticas
     * @param content informação a guardar
     */

    public void saveToFile(String content){
        FicheiroTexto text = new FicheiroTexto();
        Scanner sc = new Scanner(System.in);
        String response = null;
        do {
            System.out.println("Quer guardar os resultados da pesquisa no ficheiro de estatísticas?(sim/nao)");
            response = sc.nextLine();
        }while(!response.equalsIgnoreCase("sim") && !response.equalsIgnoreCase("nao"));
        if(response.equalsIgnoreCase("sim")){
            text.writeInFile(content+"\n");
            System.out.println("Pesquisa guardada no ficheiro");
        }
    }


    public ArrayList<Pessoa> getUtilizadores() {
        return utilizadores;
    }

    public void setUtilizadores(ArrayList<Pessoa> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public void setSalas(ArrayList<Sala> salas) {
        this.salas = salas;
    }

    public ArrayList<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(ArrayList<Curso> cursos) {
        this.cursos = cursos;
    }

    public ArrayList<Exame> getExames() {
        return exames;
    }

    public void setExames(ArrayList<Exame> exames) {
        this.exames = exames;
    }
}
