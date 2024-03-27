package com.example.copingai.controller;
import com.example.copingai.service.EntryService;
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


    @GetMapping("/chat")
    public String chatWithCoping(@RequestParam String prompt, @RequestParam Long entryId){
        return null;
    }

    public String createQuestions(@RequestParam String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        ChatMessage message1 = new ChatMessage("system", "You are a guided journalism app. Ask me questions based on my answers. You must not respond with anything other than the question. Don't make any comments about the answer. Make sure your questions are open-eneded which encourage the user to write more as this is a journal. Ensure that you end it after 2 maximum questions. After 2 questions You must absolutely stop and suggest a useful journaling exercise which would prompt the user to write more.");
        ChatMessage initFeeling = new ChatMessage("user", prompt);
        messages.add(initFeeling);
        messages.add(message1);
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

    public String createJournalingExercise(@RequestParam List<ChatMessage> convo) {
        List<ChatMessage> conversation = new ArrayList<>();

        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        ChatMessage message1 = new ChatMessage("system", "You are a guided journaling expert. Read this conversation and suggest one Journaling Prompt which will help the user reflect, learn and feel the benefits of journaling. Make sure to give plenty of content to write about. You must ONLY respond with the journaling prompt.");
        conversation.add(message1);
        conversation.addAll(convo);

        ChatCompletionRequest completionRequest = ChatCompletionRequest
                .builder()
                .messages(conversation)
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

        return returnStringBuilder.toString();
    }

    public ChatMessage getUserChatMessage (String prompt){
        return new ChatMessage("user", prompt);
    }

    public ChatMessage getAssitantChatMessage (String prompt){
        return new ChatMessage("assistant", prompt);
    }

}
