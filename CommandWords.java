import java.util.HashMap;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class CommandWords
{
    // HashMap para los string del usuario y los comandos del juego
    private HashMap<String, CommandWord> validCommands;

    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...

        validCommands = new HashMap<>();
        validCommands.put("go", CommandWord.GO);
        validCommands.put("help", CommandWord.HELP);
        validCommands.put("look", CommandWord.LOOK);
        validCommands.put("back", CommandWord.BACK);
        validCommands.put("take", CommandWord.TAKE);
        validCommands.put("drop", CommandWord.DROP);
        validCommands.put("items", CommandWord.ITEMS);
        validCommands.put("recharge", CommandWord.RECHARGE);
        validCommands.put("quit", CommandWord.QUIT);
        validCommands.put("eat", CommandWord.EAT);
        validCommands.put("recharge", CommandWord.RECHARGE);
    }

    /**
     * Check whether a given String is a valid command word. 
     * @return true if a given string is a valid command,
     * false if it isn't.
     */
    public boolean isCommand(String aString)
    {
        return validCommands.keySet().contains(aString);
    }

    /**
     * Devuelve un String con todos los comandos validos
     * disponibles actualmente en el juego.
     */
    public String getCommandList()
    {   
        String comandosDisponibles = "";
        
        for (String comando : validCommands.keySet()){
            comandosDisponibles += comando + " ";
        }
        
        return comandosDisponibles.trim();
    }
    
    /**
     * Return the Commandword associated with a word.
     * @param   commandWord The word to look up (as a String)
     * @return  The CommandWord corresponding to the String commandword, or UNKNOWN
     * if it is not a valid command word.
     */
    public CommandWord getCommanWord(String commandWord){
        CommandWord aDevolver = null;
        switch (commandWord){
            case "go":
                aDevolver = CommandWord.GO;
                break;
            case "look":
                aDevolver = CommandWord.LOOK;
                break;
            case "back":
                aDevolver = CommandWord.BACK;
                break;
            case "eat":
                aDevolver = CommandWord.EAT;
                break;
            case "take":
                aDevolver = CommandWord.TAKE;
                break;
            case "drop":
                aDevolver = CommandWord.DROP;
                break;
            case "items":
                aDevolver = CommandWord.ITEMS;
                break;
            case "recharge":
                aDevolver = CommandWord.RECHARGE;
                break;
            case "help":
                aDevolver = CommandWord.HELP;
                break;
            case "quit":
                aDevolver = CommandWord.QUIT;
                break;
            default:
                aDevolver = CommandWord.UNKNOWN;
                break;
        }
        
        return aDevolver;
    }
}
