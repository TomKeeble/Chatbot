package bot.modules;

import bot.Chatbot;
import bot.utils.Message;
import bot.utils.Module;
import bot.utils.exceptions.MalformedCommandException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static bot.utils.CONSTANTS.*;

public class Stats implements Module {
    //Constants
    private final String STATS_REGEX = ACTIONIFY("stats");
    private final Chatbot chatbot;

    public Stats(Chatbot chatbot) {
        this.chatbot = chatbot;
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        String match = getMatch(message);
        if (match.equals(STATS_REGEX)) {
            chatbot.sendMessage(getStats());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getMatch(Message message) {
        String messageBody = message.getMessage();
        if (messageBody.matches(STATS_REGEX)) {
            return STATS_REGEX;
        } else {
            return "";
        }
    }

    @Override
    public String appendModulePath(String message) {
        return chatbot.appendRootPath("modules/" + getClass().getSimpleName() + "/" + message);
    }

    public String getMinifiedStats() {
        return "Version: " + getVersion() + "\n" +
                "Java version: " + System.getProperty("java.version") + "\n" +
                "Operating System: " + System.getProperty("os.name");
    }

    private String getStats() {
        LocalDateTime startupTime = chatbot.getStartupTime();
        LocalDateTime now = LocalDateTime.now();
        long diff = now.toEpochSecond(ZoneOffset.UTC) - startupTime.toEpochSecond(ZoneOffset.UTC);
        long diffSeconds = TimeUnit.SECONDS.convert(diff, TimeUnit.SECONDS) % 60;
        long diffMinutes = TimeUnit.MINUTES.convert(diff, TimeUnit.SECONDS) % 60;
        long diffHours = TimeUnit.HOURS.convert(diff, TimeUnit.SECONDS) % 24;
        long diffDays = TimeUnit.DAYS.convert(diff, TimeUnit.SECONDS);
        return getMinifiedStats() + "\n" +
                "\n" +
                "I've been running since " + DATE_TIME_FORMATTER.format(startupTime) + "\n[" +
                (diffDays > 0 ? diffDays + " day" + (diffDays != 1 ? "s" : "") + " " : "") +
                (diffHours > 0 ? diffHours + " hour" + (diffHours != 1 ? "s" : "") + " " : "") +
                (diffMinutes > 0 ? diffMinutes + " minute" + (diffMinutes != 1 ? "s" : "") + " " : "") +
                diffSeconds + " second" + (diffSeconds != 1 ? "s" : "") +
                "]\n" +
                "Unique messages read this session: " + chatbot.getMessageLog().size();
    }
}