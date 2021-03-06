package bot.modules;

import bot.Chatbot;
import bot.utils.CommandModule;
import bot.utils.Message;
import bot.utils.exceptions.MalformedCommandException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bot.utils.CONSTANTS.ACTIONIFY;
import static bot.utils.CONSTANTS.DEACTIONIFY;

public class Shutdown implements CommandModule {
    //region Constants
    private final String SHUTDOWN_REGEX = ACTIONIFY("shutdown (\\d*)");
    private final Chatbot chatbot;
    //endregion

    public Shutdown(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    //region Overrides
    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        if (match.equals(SHUTDOWN_REGEX)) {
            Matcher matcher = Pattern.compile(SHUTDOWN_REGEX).matcher(message.getMessage());
            if (matcher.find() && matcher.group(1).equals(chatbot.getShutdownCode())) {
                chatbot.quit();
                return true;
            } else {
                throw new MalformedCommandException();
            }
        } else {
            return false;
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(SHUTDOWN_REGEX)) {
            return SHUTDOWN_REGEX;
        } else {
            return "";
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public ArrayList<String> getCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add(DEACTIONIFY(SHUTDOWN_REGEX));
        return commands;
    }

    //endregion
}