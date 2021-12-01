package utilities;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import java.util.ArrayList;

public class GPT3Impl {
    private static OpenAiService service = new OpenAiService(Config.OPENAI_KEY);
    private static final String genericQuestionPrompt = "I am a highly intelligent question answering bot. If you ask me a question that is rooted in truth, I will give you the answer. If you ask me a question that is nonsense, trickery, or has no clear answer, I will respond with \"Unknown\".\n" +
            "Q: What is human life expectancy in the United States?\n" +
            "A: Human life expectancy in the United States is 78 years.\n" +
            "Q: Who was president of the United States in 1955?\n" +
            "A: Dwight D. Eisenhower was president of the United States in 1955.\n" +
            "Q: Which party did he belong to?\n" +
            "A: He belonged to the Republican Party.\n" +
            "Q: What is the square root of banana?\n" +
            "A: Unknown\n" +
            "Q: How does a telescope work?\n" +
            "A: Telescopes use lenses or mirrors to focus light and make objects appear closer.\n" +
            "Q: Where were the 1992 Olympics held?\n" +
            "A: The 1992 Olympics were held in Barcelona, Spain.\n" +
            "Q: How many squigs are in a bonk?\n" +
            "A: Unknown\n" +
            "Q: ";
    private static final String friendlyChatResponse = "The following is a conversation with an AI assistant. The assistant is funny and helpful.\n";
    public static String getGenericAnswer(String genericQuestion) {
        genericQuestion += "\nA: ";
        genericQuestion = genericQuestionPrompt + genericQuestion;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(genericQuestion)
                .echo(true)
                .maxTokens(100)
                .topP(1.0)
                .stop(new ArrayList<>(){{add("\n");}})
                .temperature(0.0)
                .build();
        for(CompletionChoice choice : service.createCompletion("davinci", completionRequest).getChoices()) {
            return choice.getText().substring(choice.getText().lastIndexOf("A:")).replace("A: ", "");
        }
        return "Unknown";
    }
    public static String getFriendResponse(String input){
        input = friendlyChatResponse + "Human: " + input + "\nAI: ";
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(input)
                .echo(true)
                .maxTokens(150)
                .topP(1.0)
                .stop(new ArrayList<>(){{add("\n"); add("Human:"); add("AI:");}})
                .temperature(0.9)
                .presencePenalty(0.6)
                .build();
        for(CompletionChoice choice : service.createCompletion("davinci", completionRequest).getChoices()) {
            return choice.getText().substring(choice.getText().lastIndexOf("AI:")).replace("AI: ", "");
        }
        return "Unknown";
    }
}
