# Test URL

http://localhost:7000/gpt?prompt=how%20is%20the%20weather



# Processing Example Code

**Tips:Rememeber to deal with character encoding**

```
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

String gptResponse;
String prompt = "What is the weather like today?";

void setup() {
  size(400, 200);

  try {
    String encodedPrompt = URLEncoder.encode(prompt, "UTF-8");
    

    String url = "http://localhost:7000/gpt?prompt=" + encodedPrompt;
    
    gptResponse = join(loadStrings(url), " ");
    
    println("GPT's answer: " + gptResponse);

  } catch (UnsupportedEncodingException e) {
    println("URL encode failed: " + e.getMessage());
  } catch (Exception e) {
    println("respone failed: " + e.getMessage());
  }
}

void draw() {
  background(255);

  textSize(12);
  fill(0);
  text("GPT's answer: " + gptResponse, 10, 50, width - 20, height - 20);
}
```

