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
@CrossOrigin(origins = "*")
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
    public NewChatResponse chatWithCoping(@RequestParam String prompt, @RequestParam Long entryId){
        String newQuestion = "";
        newQuestion = createQuestions(prompt, entryId);
        int qCount = entryService.getQuestionCountForAnEntry(entryId);
        return new NewChatResponse(qCount, newQuestion);

    }

    @GetMapping ("/instaprompt")
    public NewChatResponse getInstaPrompt(@RequestParam Long entryId, @RequestParam String prompt){
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage instapromt = new ChatMessage("user", "Create a one-time journaling prompt about the theme '" + prompt + "'. Make sure it's easy to understand, clear and simple. invite the writer to connect with their emotions and feel something when they reflect. It should be as specific as possible, it should be about a specific moment that yields deep insights.  Encourage exploring why something inspires the theme. Finally, it should nudge the person to consider how they can integrate the insights and lessons from their reflections in their daily life and future.");
        messages.add(instapromt);
        String journalingPrompt = getNextQuestion(messages);
        entryService.saveEntryAnswer(journalingPrompt, entryId);
        return new NewChatResponse(1, journalingPrompt);
    }

    public String createQuestions(String prompt, Long entryId) {
        Entry entry = entryService.findAnEntryById(entryId);
        User user = userService.findById(entry.getUserId());
        int maxQuestion = user.getMaxQuestions();
        List<ChatMessage> messages = new ArrayList<>();
        int questionCount = entryService.getQuestionCountForAnEntry(entryId);
        System.out.println("The QUESTION COUNT IS:" + questionCount);
        if (questionCount == 0){
            //entryService.deleteEntryAllQuestionsAndAnswers(entryId);
            //prompt = "I feel " + prompt;
            ChatMessage initFeeling = new ChatMessage("user", prompt);
            String sysPrompt = "You're a journaling guide. Ask open-ended questions based on user responses. Respond only with questions. Avoid comments. Encourage reflection and detailed writing.";
            entryService.saveEntryQuestion("system:" + sysPrompt, entryId);
            entryService.saveEntryQuestion("User:" + prompt, entryId);
            ChatMessage message1 = new ChatMessage("system",sysPrompt);
            messages.add(message1);
            messages.add(initFeeling);
            System.out.println("messages:" + messages);
        }
        else if(questionCount == maxQuestion){
            entryService.saveEntryQuestion("User:" + prompt, entryId);
            ChatMessage message1 = new ChatMessage("system", """
                    You are a guided journaling expert. Read this conversation and respond only with a Journaling Prompt which will help the user reflect, learn and feel the benefits of journaling. Make sure to give plenty of content to write about.     
                    You must use this format for your answer: 
                    Example response: Journaling Prompt - write about three things that made you happy this week and why""");
            System.out.println("messages before Journaling Prompt" + messages);
            messages.addAll(getConvoForAnEntry(entryId));
            messages.remove(0);
            messages.add(message1);
            System.out.println("messages after adding JP:" + messages);
            String journalingPrompt = getNextQuestion(messages);
            entryService.saveEntryAnswer(journalingPrompt, entryId);
            entryService.increaseQuestionCount(entryId);
            return journalingPrompt;
        }
        else if(questionCount > maxQuestion){
            return "Please refer to the Journaling Prompt in your conversation to continue your entry";
        }
        else {
            entryService.saveEntryQuestion("User: " + prompt, entryId);
            messages.addAll(getConvoForAnEntry(entryId));
        }
        System.out.println("messages:" + messages);
        String nextQuestion = getNextQuestion(messages);
        entryService.saveEntryQuestion("Coping: " + nextQuestion, entryId);
        entryService.increaseQuestionCount(entryId);
        return nextQuestion;
    }

    public String getNextQuestion (List<ChatMessage> messagesForQuestion) {
        OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        ChatCompletionRequest completionRequest = ChatCompletionRequest
                .builder()
                .messages(messagesForQuestion)
                .model(model)
                .temperature(1.0)
                .maxTokens(1500)
                .frequencyPenalty(0.0)
                .presencePenalty(0.0)
                .n(1)
                .build();
        List<ChatCompletionChoice> choices = service.createChatCompletion(completionRequest).getChoices();
        System.out.println(choices.toString());
        return choices.get(0).getMessage().getContent();
    }

    public List<ChatMessage> getConvoForAnEntry( Long entryId){
        List<String> questionsForEntry = entryService.getQuestionListForAnEntry(entryId);
        List<ChatMessage> finalConvo = new ArrayList<>();
        for (int i = 0; i < questionsForEntry.size(); i++) {
            if(i==0){
                String question = questionsForEntry.get(i);
                ChatMessage systemChat = getSystemChatMessage(question);
                finalConvo.add(systemChat);
            }
            else if(i%2 != 0) {
                String userText = questionsForEntry.get(i);
                ChatMessage userChat = getUserChatMessage(userText);
                finalConvo.add(userChat);
            }
            else {
                String assistant = questionsForEntry.get(i);
                ChatMessage assistChat = getAssitantChatMessage(assistant);
                finalConvo.add(assistChat);
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
    public ChatMessage getSystemChatMessage (String prompt){
        return new ChatMessage("system", prompt);
    }


}
