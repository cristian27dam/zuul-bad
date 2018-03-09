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
 * @author  Michael KÃ¶lling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room entradaSalida, corredor1, salaAgua, salaFuego, corredor2, corredor3, salaElectrica, salaLLave, corredor4, corredor5;

        // create the rooms
        entradaSalida = new Room("Entrada (y salida) de la mazmorra");
        corredor1 = new Room("Corredor nº1");
        salaAgua = new Room("Sala de conductos de agua");
        salaFuego = new Room("Sala en llamas");
        corredor2 = new Room("Corredor nº2");
        corredor3 = new Room("Corredor nº3");
        salaElectrica = new Room("Sala de energía eléctrica");
        salaLLave = new Room ("Sala del cofre");
        corredor4 = new Room("Corredor nº4");
        corredor5 = new Room("Corredor nº5");

        // initialise room exits (norte, este, sureste, sur, oeste, noroeste)
        entradaSalida.setExits(corredor1, null, null, null, null, salaAgua);
        corredor1.setExits(null, salaFuego, null, entradaSalida, salaAgua, corredor5);
        salaAgua.setExits(corredor5, corredor1, entradaSalida, null, null, null);
        salaFuego.setExits(corredor2, null, null, null, corredor1, null);
        corredor2.setExits(corredor3, null, null, salaFuego, null, null);
        corredor3.setExits(null, salaElectrica, null, corredor2, salaLLave, null);
        salaElectrica.setExits(null, null, null, null, corredor3, null);
        salaLLave.setExits(null, corredor3, null, null, corredor4, null);
        corredor4.setExits(null, salaLLave, null, corredor5, null, null);
        corredor5.setExits(corredor4, null, corredor1, salaAgua, null, null);

        // start game outside
        currentRoom = entradaSalida;
    }

    /**
     *  Main play routine.  Loops until end of play.
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
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo(); // Imprime localizacion y posibles salidas
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
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
        System.out.println("pero tienes que salir de ahí..");
        System.out.println();
        System.out.println("Los comandos disponibles son:");
        System.out.println("   go quit help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
        
        // La direccion que solicita el usuario
        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            printLocationInfo(); // Imprime localizacion y posibles salidas
        }
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
    
    /**
     * Metodo privado que nos informa en que sala nos encontramos
     * e imprime las posibles salidas desde ese punto actual.
     */
    private void printLocationInfo(){
        System.out.println("Te encuentras en: " + currentRoom.getDescription());
        System.out.print("Exits: ");
        System.out.println(currentRoom.getExitString()); // Cadena para las salidas disponibles
    }
}
