import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

class Letter{
    int rez_h_pos;
    int rez_v_pos;
    int rez_num_letter;

    public void PrintWord(Session session,String word){
        session.field[rez_h_pos][rez_v_pos]=word.charAt(rez_num_letter);
    }
}

class Session{
    char[][] field;
    int skips = 0;
    int score_of_1;
    int score_of_2;
    private boolean flag;
    public Session(int N){
        field = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                field[i][j] = '_';
            }
        }
    }

    private  void CheckWord(String word, Letter letter, int i, int h_pos, int v_pos, boolean trigger, String calling_direction, int rez_h_pos, int rez_v_pos, int rez_letter_num){
        if (field[h_pos-1][v_pos]==word.charAt(i) && !calling_direction.equals("left")){  //слева
            CheckLast(word,letter,i+1,h_pos-1,v_pos,trigger,"right", rez_h_pos, rez_v_pos,rez_letter_num);
        }
        if (field[h_pos+1][v_pos]==word.charAt(i) && !calling_direction.equals("right")){  //справа
            CheckLast(word,letter,i+1,h_pos+1,v_pos,trigger,"left", rez_h_pos, rez_v_pos,rez_letter_num);
        }
        if (field[h_pos][v_pos-1]==word.charAt(i) && !calling_direction.equals("down")){  //снизу
            CheckLast(word,letter,i+1,h_pos,v_pos-1,trigger,"up", rez_h_pos, rez_v_pos,rez_letter_num);
        }
        if (field[h_pos][v_pos+1]==word.charAt(i) && !calling_direction.equals("up")){  //сверху
            CheckLast(word,letter,i+1,h_pos,v_pos+1,trigger,"down", rez_h_pos, rez_v_pos,rez_letter_num);
        }
        if(!trigger){
            if (field[h_pos-1][v_pos]=='_' && !calling_direction.equals("left")){  //слева
                CheckLast(word,letter,i+1,h_pos-1,v_pos,true,"right",h_pos-1 , v_pos,i);
            }
            if (field[h_pos+1][v_pos]=='_' && !calling_direction.equals("right")){  //справа
                CheckLast(word,letter,i+1,h_pos+1,v_pos,true,"left", h_pos+1, v_pos,i);
            }
            if (field[h_pos][v_pos-1]=='_' && !calling_direction.equals("down")){  //снизу
                CheckLast(word,letter,i+1,h_pos,v_pos-1,true,"up", h_pos, v_pos-1,i);
            }
            if (field[h_pos][v_pos+1]=='_' && !calling_direction.equals("up")){  //сверху
                CheckLast(word,letter,i+1,h_pos,v_pos+1,true,"down", h_pos, v_pos+1,i);
            }
        }
    }

    private void CheckLast(String word, Letter letter, int i, int h_pos, int v_pos, boolean trigger, String calling_direction, int rez_h_pos, int rez_v_pos, int rez_letter_num){
        if(i==word.length()-1){
            if (field[h_pos-1][v_pos]==word.charAt(i) && trigger && !calling_direction.equals("left")){  //слева
                letter.rez_h_pos=rez_h_pos;
                letter.rez_v_pos=rez_v_pos;
                letter.rez_num_letter=rez_letter_num;
                flag=true;
            }
            else if (field[h_pos+1][v_pos]==word.charAt(i) && trigger && !calling_direction.equals("right")){  //справа
                letter.rez_h_pos=rez_h_pos;
                letter.rez_v_pos=rez_v_pos;
                letter.rez_num_letter=rez_letter_num;
                flag= true;
            }
            else if (field[h_pos][v_pos-1]==word.charAt(i) && trigger && !calling_direction.equals("down")){  //снизу
                letter.rez_h_pos=rez_h_pos;
                letter.rez_v_pos=rez_v_pos;
                letter.rez_num_letter=rez_letter_num;
                flag= true;
            }
            else if (field[h_pos][v_pos+1]==word.charAt(i) && trigger && !calling_direction.equals("up")){  //сверху
                letter.rez_h_pos=rez_h_pos;
                letter.rez_v_pos=rez_v_pos;
                letter.rez_num_letter=rez_letter_num;
                flag= true;
            }
            else if(!trigger) {
                if (field[h_pos - 1][v_pos] == '_' && !calling_direction.equals("left")) {  //слева
                    letter.rez_h_pos = h_pos - 1;
                    letter.rez_v_pos = v_pos;
                    letter.rez_num_letter = i;
                    flag = true;
                } else if (field[h_pos + 1][v_pos] == '_' && !calling_direction.equals("right")) {  //справа
                    letter.rez_h_pos = h_pos + 1;
                    letter.rez_v_pos = v_pos;
                    letter.rez_num_letter = i;
                    flag = true;
                } else if (field[h_pos][v_pos - 1] == '_' && !calling_direction.equals("down")) {  //снизу
                    letter.rez_h_pos = h_pos;
                    letter.rez_v_pos = v_pos - 1;
                    letter.rez_num_letter = i;
                    flag = true;
                } else if (field[h_pos][v_pos + 1] == '_' && !calling_direction.equals("up")) {  //сверху
                    letter.rez_h_pos = h_pos;
                    letter.rez_v_pos = v_pos + 1;
                    letter.rez_num_letter = i;
                    flag = true;
                }
            }
        }
        else
            CheckWord(word,letter,i,h_pos,v_pos,trigger,calling_direction, rez_h_pos, rez_v_pos,rez_letter_num);
    }

    public void StartTurn(int player, int N,Letter letter) {
        PrintDeck(N);
        Scanner scanner=new Scanner(System.in);
        System.out.println("1-Написать слово\n2-Пропустить ход");
        int k=scanner.nextInt();
        switch (k) {
            case 1:
                flag = false;
                System.out.println("Введите слово:");
                String word = scanner.next();
                System.out.println("Выберите номер строки с которой хотите начать ввод слова(горизонталь)");
                int h_position = scanner.nextInt();
                System.out.println("Выберите номер столбца с которого хотите начать ввод слова(вертикаль)");
                int v_position = scanner.nextInt();
                scanner.nextLine();
                if (Slova.hashSet.contains(word)) {
                    int h_pos = h_position - 1;
                    int v_pos = v_position - 1;
                    if (field[h_pos][v_pos] == word.charAt(0)) {
                        CheckWord(word,letter, 1, h_pos, v_pos, false,"",-1,-1,-1);
                        if (flag) {
                            letter.PrintWord(this,word);
                        }
                    } else if(field[h_pos][v_pos] =='_'){
                        CheckWord(word, letter,1, h_pos, v_pos, true,"",h_pos,v_pos, 0);
                        if (flag) {
                            field[h_pos][v_pos]=word.charAt(0);
                            letter.PrintWord(this,word);
                        }
                    }
                    if (!flag){
                        System.out.println("Неправильно указано место ввода слова или само слово нельзя составить!");
                        StartTurn(player,N,letter);
                    }
                    if (flag) {
                        if (player == 1) {
                            score_of_1 += word.length();
                        } else {
                            score_of_2 += word.length();
                        }
                        skips=0;
                    }
                } else {
                    System.out.println("Такого слова нет в соловаре(");
                    StartTurn(player,N,letter);
                }
                break;
            case 2:
                skips+=1;
                break;
            default:
                System.out.println("Вы ошиблись при выборе варианта действия!");
                StartTurn(player,N,letter);
                break;
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    private void PrintDeck(int deck){
        System.out.print("   ");
        for (int j=0; j<deck; j++){
            System.out.print((j+1)%10+".");
        }
        System.out.println();
        for (int i = 0; i < deck; i++) {
            System.out.printf("%2d",i+1);
            for (int j = 0; j < deck; j++) {
                System.out.print("|" + field[i][j]);
            }
            System.out.println("|");
        }
    }

    public boolean SetFirstWord(int h_pos, int v_pos, String word, char direction){
        if(word.length()<=field.length && (v_pos+word.length()<field.length && direction=='г'
                || h_pos+word.length()<field.length && direction=='в')) {
            for (int i = 0; i < word.length(); i++) {
                if (direction == 'г') {
                    field[h_pos - 1][v_pos + i - 1] = word.charAt(i);
                }
                if (direction == 'в') {
                    field[h_pos + i - 1][v_pos - 1] = word.charAt(i);
                }
            }
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            return true;
        }
        else {
            System.out.println("Слово не поместится");
            System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
            return false;
        }
    }
}

public class Slova {
    static public HashSet<String> hashSet = new HashSet<>();
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите размер поля(NxN):");
        int N = scanner.nextInt();
        Session session = new Session(N);
        FillHashSet();
        Letter letter=new Letter();
        boolean flag=false;
        while(!flag) {
            System.out.println("Введите слово которое поместите в центр поля:");
            String word = scanner.next();
            System.out.println("Выберите номер строки с которой хотите начать ввод слова(горизонталь)");
            int h_position = scanner.nextInt();
            System.out.println("Выберите номер столбца с которого хотите начать ввод слова(вертикаль)");
            int v_position = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Выберите направление(горизонтально-'г', вертикально-'в':");
            char direction = scanner.nextLine().charAt(0);
            if (hashSet.contains(word)) {
                flag = session.SetFirstWord(h_position, v_position, word, direction);
            }
        }
        while(session.skips!=2){

            System.out.println("ХОД ПЕРВОГО ИГРОКА:\n");
            session.StartTurn(1,N,letter);
            System.out.println();
            System.out.println("У первого игрока "+session.score_of_1+" очков, у втрого "+session.score_of_2+" очков\n");
            if (session.skips!=2) {
                System.out.println("ХОД ВТОРОГО ИГРОКА:\n");
                session.StartTurn(2,N,letter);
                System.out.println();
                System.out.println("У первого игрока "+session.score_of_1+" очков, у втрого "+session.score_of_2+" очков\n");
            }
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        if(session.score_of_1>session.score_of_2){
            System.out.println("Игра окончена, поздравляем игрока 1 с победой со счетом: "+session.score_of_1+"-"+session.score_of_2+"!");
        }
        else if(session.score_of_1<session.score_of_2){
            System.out.println("Игра окончена, поздравляем игрока 2 с победой со счетом: "+session.score_of_1+"-"+session.score_of_2+"!");
        }
        else{
            System.out.println("Игра окончена, у игроков ничья: "+session.score_of_1+"-"+session.score_of_2+"!");
        }
    }

    private static void FillHashSet() throws FileNotFoundException {
        Scanner scanf = new Scanner(new File("slovar.txt"));
        while (scanf.hasNext()) {
            hashSet.add(scanf.nextLine());
        }
    }
}