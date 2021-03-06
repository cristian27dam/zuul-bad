/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{   
    private Parser parser;
    // Jugador
    private Player player;

    /**
     * Constructor de Game que crea un nuevo Player e inicializa el mapeo de las Room y el Parser.
     */
    public Game() 
    {   
        player = new Player(createRooms());
        parser = new Parser();
    }

    /**
     * Crea e inicializa las salas de las que se compone el mapa del juego.
     * 
     * @return  Un objeto Room que representa la sala en la que aparece el jugador por primera vez (Spawn).
     */
    private Room createRooms()
    {
        Room entradaSalida, corredor1, salaAgua, salaFuego, corredor2, corredor3, salaElectrica, salaLLave, corredor4, corredor5;

        // Inicializacion de las salas de la mazmorra con su descripcion
        entradaSalida = new Room("Entrada/Salida: Estas confundido y mareado.. Ves una puerta cerrada detras de ti.. \nNotas algo de humedad en el ambiente..");
        corredor1 = new Room("Corredor 1: Un pasadizo donde se escucha ruido cerca..");
        salaAgua = new Room("Sala de Agua: Te encuentras en una sala llena de canyerias desde donde parece \nque se puede controlar el sistema hidraulico de este lugar..");
        salaFuego = new Room("Sala de Fuego: En esta sala ves llamas por todas partes y resulta sofocante estar mucho tiempo aqui..");
        corredor2 = new Room("Corredor 2: Un pasadizo con extranyos simbolos dibujados en la pared..\nEn el techo puedes ver una trampilla con un gran simbolo iluminado");
        corredor3 = new Room("Corredor 3: A tu alrededor hay objetos que parecen demasiado modernos para este sitio \n que tienen grabados parecidos a los que has visto..");
        salaElectrica = new Room("Sala electrica: Parece una antigua sala desde donde gestionaban la energia.. \nVes maquinas de todo tipo que desconoces..");
        salaLLave = new Room ("Sala misteriosa: Puede que aqui encuentres algo que te ayude a salir de este lugar.. \nVes un pilar con una hendidura conocida..");
        corredor4 = new Room("Corredor 4: Los dibujos de las paredes ahora parecen brillar..");
        corredor5 = new Room("Corredor 5: Parece que este lugar comunica con la sala de control de agua \nporque recuerdas ese sonido con claridad..");

        // Inicializacion de los items de cada sala
        salaLLave.addItem(new Item("Un cofre en medio de la sala", 3000, "cofre", false));
        corredor3.addItem(new Item("Una una bateria tirada en una esquina", 1000, "bateria", true));
        salaAgua.addItem(new Item("Una llave de paso que parece funcionar", 500, "llavePaso", false));
        salaAgua.addItem(new Item("Hay un objeto brillante en una esquina", 500, "amuleto", true));
        corredor5.addItem(new Item("Una carta medio quemada sobre una mesa", 200, "carta",true));
        salaElectrica.addItem(new Item("Una llave sobresale de una muesca de uno de los pilares de la sala", 300, "llaveCofre", true));
        salaLLave.addItem(new Item("Otra llave con los simbolos que has visto cuelga de la pared", 300, "llaveDesconocida",true));

        // Mapeo de cada salida de cada sala al momento de creacion
        entradaSalida.setSalidaIndividual("north", corredor1);
        entradaSalida.setSalidaIndividual("north-west", salaAgua);
        corredor1.setSalidaIndividual("south", entradaSalida);
        corredor1.setSalidaIndividual("west", salaAgua);
        corredor1.setSalidaIndividual("east", salaFuego);
        salaAgua.setSalidaIndividual("east", corredor1);
        salaAgua.setSalidaIndividual("south-east", entradaSalida);
        salaFuego.setSalidaIndividual("west", corredor1);
        salaFuego.setSalidaIndividual("north", corredor2);
        corredor2.setSalidaIndividual("north", corredor3);
        corredor2.setSalidaIndividual("south", salaFuego);
        corredor3.setSalidaIndividual("south", corredor2);
        corredor3.setSalidaIndividual("east", salaElectrica);
        corredor3.setSalidaIndividual("west", salaLLave);
        salaElectrica.setSalidaIndividual("west", corredor3);
        salaLLave.setSalidaIndividual("east", corredor3);
        salaLLave.setSalidaIndividual("west", corredor4);
        corredor4.setSalidaIndividual("east", salaLLave);
        corredor4.setSalidaIndividual("south", corredor5);
        corredor5.setSalidaIndividual("north", corredor4);
        corredor5.setSalidaIndividual("south", salaAgua);
        corredor5.setSalidaIndividual("south-east", corredor1);

        // Por mi planteamiento del mapa y sus "mecanicas" no se pueden mapear estas posibles direcciones
        // a pesar de que existan pero como no tenemos nada implementado las especifico igualmente
        salaAgua.setSalidaIndividual("north", corredor5);
        corredor1.setSalidaIndividual("north-west", corredor5);

        return entradaSalida; // Devuelve el spawn del jugador al constructor a la vez que lo crea e iniciliza
    }

    /**
     *  Metodo que controla el inicio y fin del juego alternando entre recibir y comprobar si el comando
     *  introducido es valido y en ese caso invocar los metodos oportunos, ya sea goRoom() (moverse), 
     *  printHelp() (ayuda al usuario) o invertir la variable finished usando "quit".
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     * Ademas devuelve la informacion para el jugador en la primera sala de aparicion.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Bienvenido a World of Signs!");
        System.out.println("World of Signs es un simple juego adaptado para nuestra clase de Java! ^^");
        System.out.println("Escribe 'help' para mas informacion.");
        System.out.println();
        player.look(); // Imprime localizacion y posibles salidas
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        System.out.println("=========================");
        boolean wantToQuit = false;

        // String commandWord = command.getCommandWord();
        CommandWord commandWord = command.getCommandWord();

        switch (commandWord){
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;
            case HELP:
                printHelp();
                break;
            case GO:
                player.goRoom(command.getSecondWord());
                break;
            case LOOK:
                player.look();
                break;
            case EAT:
                player.eat();
                break;
            case TAKE:
                player.pickUpItem(command.getSecondWord());
                break;
            case DROP:
                player.dropItem(command.getSecondWord());
                break;
            case ITEMS:
                player.showItems();
                break;
            case QUIT:
                wantToQuit = quit(command);
                break;
            case BACK:
                player.backToRoom();
                break;
            case RECHARGE:
                player.changePlayerInventory(command.getSecondWord());
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Te despiertas en una mazmorra. No sabes que ha pasado..");
        System.out.println("pero tienes que salir de ah�..");
        System.out.println();
        System.out.println("Los comandos disponibles son:");
        System.out.println(parser.showCommands()); // Imprime tantos comandos como existan en la clase CommandWords
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
