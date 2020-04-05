import java.util.Random;
import java.util.Scanner;//// сделать добор букв в конце хода

class Player{
    char[] letters= new char[]{'0','0','0','0','0','0','0'};
    int score;
}

public class Scrabble {
    static int skips = 0;
    static boolean first_turn=true;
    static Scanner scan = new Scanner(System.in);
    static int plates_in_heap=104;
    static char[][] field;
    static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    static int[] letter_costs = new int[]{1, 3, 1, 3, 2, 1, 3, 5, 5, 1, 4, 2, 2, 2, 1, 1, 2, 1, 1, 1, 2, 10, 5, 5, 5, 8, 10, 10, 4, 3, 8, 8, 3};
    static char[] heap_with_letters = new char[]{'а', 'а', 'а', 'а', 'а', 'а', 'а', 'а', 'б', 'б', 'в', 'в', 'в', 'в', 'г', 'г', 'д', 'д',
            'д', 'д', 'е', 'е', 'е', 'е', 'е', 'е', 'е', 'е', 'ё', 'ж', 'з', 'з', 'и', 'и', 'и', 'и', 'и', 'й', 'к', 'к', 'к', 'к', 'л', 'л',
            'л', 'л', 'м', 'м', 'м', 'н', 'н', 'н', 'н', 'н', 'о', 'о', 'о', 'о', 'о', 'о', 'о', 'о', 'о', 'о', 'п', 'п', 'п', 'п', 'р', 'р',
            'р', 'р', 'р', 'с', 'с', 'с', 'с', 'с', 'т', 'т', 'т', 'т', 'т', 'у', 'у', 'у', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ы', 'ь',
            'ь', 'э', 'ю', 'ю', 'я', 'я', 'я'};                               //*-пустая фишка, всего 104 фишки

    public static void main(String[] args) {
        System.out.println("Введите размер поля(NxN):");
        int N = scan.nextInt();
        Scrabble.field = new char[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                field[i][j] = '_';
            }
        }
        Player player1 = new Player();
        Player player2 = new Player();
        //начало игры

        TakePlates(player1,7);
        TakePlates(player2,7);

        while (Scrabble.skips < 4 && plates_in_heap > 0) {
            System.out.println("ХОД ПЕРВОГО ИГРОКА:");
            StartTurn(player1,N);
            System.out.println();
            System.out.println("У первого игрока "+player1.score+" очков, у втрого "+player2.score+" очков");
            if (Scrabble.skips < 4 && plates_in_heap > 0) {
                System.out.println("ХОД ВТОРОГО ИГРОКА:");
                StartTurn(player2,N);
                System.out.println();
            }
        }
        if(player1.score>player2.score){
            System.out.println("Игра окончена, поздравляем игрока 1 с победой со счетом: "+player1.score+"-"+player2.score+"!");
        }
        else if(player2.score>player1.score){
            System.out.println("Игра окончена, поздравляем игрока 2 с победой со счетом: "+player2.score+"-"+player1.score+"!");
        }
        else{
            System.out.println("Игра окончена, у игроков ничья: "+player1.score+"-"+player2.score+"!");
        }
    }


    public static void StartTurn(Player player,int N){
        boolean end_turn=false;
        int k;
        while (end_turn!=true) {
            Scrabble.PrintDeck(N);
            System.out.println();
            System.out.println("Буквы в руке:");
            System.out.println();
            Scrabble.PrintHand(player);
            System.out.println("1-Ввести слово\n2-Заменить буквы и закончить ход");
            k=scan.nextInt();
            switch (k){
                case 1:
                    System.out.println("Выберите номер строки с которой хотите начать ввод слова(горизонталь)");
                    int h_position=scan.nextInt();
                    System.out.println("Выберите номер столбца с которого хотите начать ввод слова(вертикаль)");
                    int v_position=scan.nextInt();
                    scan.nextLine();
                    System.out.println("Выберите направление(горизонтально-'г', вертикально-'в':");
                    char direction=scan.nextLine().charAt(0);
                    System.out.println("Введите слово которое хотите составить:");
                    String word=scan.nextLine();
                    int cost_of_word=CreateWord(player,h_position,v_position,word,direction);
                    if(cost_of_word==-1){
                        System.out.println("Вы ошиблись при попытке написать слово!");
                    }
                    else if(cost_of_word==-2) {
                        System.out.println("Первое слово должно проходить через центральную ячейку поля!");
                        }
                    else{
                            player.score+=cost_of_word;
                        }
                    skips=0;
                    break;
                case 2:
                    System.out.println("Сколько фишек вы хотите заменить?");
                    int count = scan.nextInt();
                    scan.nextLine();
                    char letter;
                    for (int i=1; i<=count; i++) {
                        System.out.println("Введите "+i+" букву которую хотите заменить:");
                        letter=scan.nextLine().charAt(0);
                        ThrowPlates(player, letter);
                    }
                    TakePlates(player,count);
                    skips+=1;
                    end_turn=true;
                    break;
                default:
                    System.out.println("Вы ввели неправильную цифру!");
                    break;
            }
        }
    }

    public static int CreateWord(Player player,int h_pos,int v_pos,String word,char direction) {
        boolean cross_existed_word = false;
        int count = 0;
        if(first_turn){//проверка на первый ход
            boolean flag=false;
            for (int i=0; i<word.length(); i++){
                if((direction == 'г' && h_pos==(Scrabble.field.length/2)+1 && v_pos+i==(Scrabble.field.length/2)+1)
                        || (direction == 'в' && h_pos+i==(Scrabble.field.length/2)+1 && v_pos==(Scrabble.field.length/2)+1))
                {
                    for (int j = 0; j < word.length(); j++) {
                        if (CheckInHand(player.letters, word.charAt(j)) == true) {
                            Scrabble.first_turn = false;
                            cross_existed_word=true;
                        } else {
                            Scrabble.first_turn=true;
                            cross_existed_word=false;
                            flag=true;
                            break;
                        }
                    }
                    break;
                }
            }
            if(flag) {
                return -2;
            }
        }
        else {
            for (int i = 0; i < word.length(); i++) {//проверка на выполнение правил написания слова
                if (direction=='г') {
                    if (word.charAt(i) == Scrabble.field[h_pos - 1][v_pos - 1 + i]) {
                        for (int j = 0; j < word.length(); j++) {
                            if (i != j && CheckInHand(player.letters, word.charAt(j)) == true) {
                                cross_existed_word = true;
                            } else if (i==j || word.charAt(j) == Scrabble.field[h_pos - 1][v_pos - 1 + j])
                                {
                                    continue;
                                }
                            else {
                                cross_existed_word = false;
                                break;
                            }
                        }
                        break;
                    }
                }
                if (direction=='в') {
                    if (word.charAt(i) == Scrabble.field[h_pos - 1 + i][v_pos - 1]) {
                        for (int j = 0; j < word.length(); j++) {
                            if (i != j && CheckInHand(player.letters, word.charAt(j)) == true) {
                                cross_existed_word = true;
                            }
                            else if (i==j || word.charAt(j) == Scrabble.field[h_pos - 1 + j][v_pos - 1])
                                {
                                    continue;
                                }
                            else {
                                cross_existed_word = false;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (cross_existed_word) {                    //написание слова
            for (int i = 0; i < word.length(); i++) {
                if (direction == 'г') {
                    for (int j = 0; j < player.letters.length; j++) {
                        if (player.letters[j] == word.charAt(i)) {
                            for (int k = 0; k < Scrabble.alphabet.length(); k++) {
                                if(word.charAt(i) != Scrabble.field[h_pos - 1][v_pos + i - 1]) {
                                    if (Scrabble.alphabet.charAt(k) == player.letters[j]) {    //оценка буквы
                                        count += Scrabble.letter_costs[k];
                                        Scrabble.field[h_pos - 1][v_pos + i - 1] = word.charAt(i);
                                        break;
                                    }
                                }
                            }
                            player.letters[j] = '0';
                            break;
                        }
                    }
                }
                if (direction == 'в') {
                    for (int j = 0; j < player.letters.length; j++) {
                        if (player.letters[j] == word.charAt(i) && Scrabble.field[h_pos + i - 1][v_pos - 1]=='_') {
                            for (int k = 0; k < Scrabble.alphabet.length(); k++) {
                                if(word.charAt(i) != Scrabble.field[h_pos+i - 1][v_pos - 1]) {
                                    if (Scrabble.alphabet.charAt(k) == player.letters[j]) {    //оценка буквы
                                        count += Scrabble.letter_costs[k];
                                        Scrabble.field[h_pos + i - 1][v_pos - 1] = word.charAt(i);
                                        break;
                                    }
                                }
                            }
                            player.letters[j] = '0';
                            break;
                        }
                    }
                }
            }
            TakePlates(player, word.length());
            return count;
        }
        else{
            return -1;
        }
    }

    public static boolean CheckInHand(char[] array,char letter){
        boolean finded=false;
        for(int i=0; i< array.length; i++)
            if (array[i]==letter){
                finded=true;
                break;
            }
        return finded;
    }

    public static void PrintDeck(int deck){
        System.out.print("   ");
        for (int j=0; j<deck; j++){
            System.out.print((j+1)%10+".");
        }
        System.out.println();
        for (int i = 0; i < deck; i++) {
            System.out.printf("%2d",i+1);
            for (int j = 0; j < deck; j++) {
                System.out.print("|" + Scrabble.field[i][j]);
            }
            System.out.println("|");
        }
    }

    public static void PrintHand(Player player){
        for(int i=0; i<player.letters.length-1; i++){
            System.out.print(player.letters[i]+", ");
        }
        System.out.println(player.letters[player.letters.length-1]);
    }

    public static void ThrowPlates(Player player,char letter){
        for(int i=0; i<player.letters.length; i++){
            if(player.letters[i]==letter)
                player.letters[i]='0';
            break;
        }
    }

    public static void TakePlates(Player player,int count){
        Random random = new Random();
        int letter_number_in_heap=0;
        for(int i=0; i<count; i++){
            boolean has_plate_with_number=false;
            while (has_plate_with_number != true){
                letter_number_in_heap=random.nextInt(103);
                if (Scrabble.heap_with_letters[letter_number_in_heap]!='0'){
                    has_plate_with_number=true;
                }
            }
            for(int j=0; j<player.letters.length; j++){
                if(player.letters[j]=='0'){
                    player.letters[j]=Scrabble.heap_with_letters[letter_number_in_heap];
                    Scrabble.heap_with_letters[letter_number_in_heap]='0';
                    Scrabble.plates_in_heap-=1;
                    break;
                }
            }
        }
    }
}
