package com.example.copingai.controller;
import com.example.copingai.entities.Entry;
import com.example.copingai.entities.User;
import com.example.copingai.service.EntryService;
import com.example.copingai.service.UserService;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@SuppressWarnings({"unused", "UnusedReturnValue"})
@Tag(name = "GPTQs Api")
@RequestMapping("/gptq")
public class GPTQController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    EntryService entryService;

    @Autowired
    UserService userService;


    @GetMapping("/chat")
    public String chatWithCoping(@RequestParam String prompt, @RequestParam Long entryId){
        String newQuestion = "";
        newQuestion = createQuestions(prompt,entryId);
        entryService.saveEntryQuestion(newQuestion, entryId);
        entryService.increaseQuestionCount(entryId);

        return newQuestion;
    }

    public String createQuestions(@RequestParam String prompt, Long entryId) {
        Entry entry = entryService.findAnEntryById(entryId);
        User user = userService.findById(entry.getUserId());
        int maxQuestion = user.getMaxQuestions();
        List<ChatMessage> messages = new ArrayList<>();
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        int questionCount = entryService.getQuestionCountForAnEntry(entryId);

        if (questionCount == 0){
            prompt = "I feel " + prompt;
            entryService.saveEntryAnswer(prompt, entryId);
            ChatMessage initFeeling = new ChatMessage("user", prompt);
            ChatMessage message1 = new ChatMessage("system", "You are a guided journalism app. Ask me questions based on my answers. You must not respond with anything other than the question. Don't make any comments about the answer. Make sure your questions are open-eneded which encourage the user to write more as this is a journal. Ensure that you end it after 2 maximum questions. After 2 questions You must absolutely stop and suggest a useful journaling exercise which would prompt the user to write more.");
            messages.add(message1);
            messages.add(initFeeling);
        }
        else if(questionCount >= maxQuestion){
            ChatMessage message1 = new ChatMessage("system", """
                    You are a guided journaling expert. Read this conversation and respond only with a Journaling Prompt which will help the user reflect, learn and feel the benefits of journaling. Make sure to give plenty of content to write about.
                                
                    Your response should be specifically in the following example format
                                
                    Example response: 'Journaling Prompt - write about three things that made you happy this week and why'     """);
            messages.add(message1);
            messages.addAll(getConvoForAnEntry(entryId));
        }
        else if(questionCount > maxQuestion){
            return "Please refer to the Journaling Prompt in your conversation to continue your entry";
        }
        else {
            ChatMessage initFeeling = new ChatMessage("user", prompt);
            ChatMessage message1 = new ChatMessage("system", "You are a guided journalism app. Ask me questions based on my answers. You must not respond with anything other than the question. Don't make any comments about the answer. Make sure your questions are open-eneded which encourage the user to write more as this is a journal. Ensure that you end it after 2 maximum questions. After 2 questions You must absolutely stop and suggest a useful journaling exercise which would prompt the user to write more.");
            messages.add(message1);
            messages.add(initFeeling);
        }

            ChatCompletionRequest completionRequest = ChatCompletionRequest
                    .builder()
                    .messages(messages)
                    .model(model)
                    .temperature(1.0)
                    .maxTokens(1500)
                    .frequencyPenalty(0.0)
                    .presencePenalty(0.0)
                    .n(1)
                    .build();
            List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
            StringBuilder returnStringBuilder = new StringBuilder();
            for (ChatCompletionChoice choice : choices) {
                returnStringBuilder.append(choice.getMessage().getContent()).append(System.lineSeparator());
            }

            messages.add(new ChatMessage("assistant", returnStringBuilder.toString()));
            return returnStringBuilder.toString();
    }

    public List<ChatMessage> getConvoForAnEntry( Long entryId){
        List<String> questionsForEntry = entryService.getQuestionListForAnEntry(entryId);
        List<String> answersForEntry = entryService.getAnswerListForAnEntry(entryId);
        List<ChatMessage> finalConvo = new ArrayList<>();
        for (int i = 0; i < answersForEntry.size(); i++) {
            String answer = answersForEntry.get(i);
            ChatMessage answerChat = getUserChatMessage(answer);
            finalConvo.add(answerChat);

            if (i < questionsForEntry.size()) {
                String question = questionsForEntry.get(i);
                ChatMessage questionChat = getAssitantChatMessage(question);
                finalConvo.add(questionChat);
            }
        }
        return finalConvo;
    }

    public ChatMessage getUserChatMessage (String prompt){
        return new ChatMessage("user", prompt);
    }

    public ChatMessage getAssitantChatMessage (String prompt){
        return new ChatMessage("assistant", prompt);
    }

}
