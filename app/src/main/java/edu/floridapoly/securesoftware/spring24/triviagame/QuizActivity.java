package edu.floridapoly.securesoftware.spring24.triviagame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private long startTime;
    private String difficulty;

    private TextView questionTextView;
    private Button option1Button;
    private Button option2Button;
    private Button option3Button;
    private Button option4Button;


    private boolean quizOngoing = true; // Flag to track quiz state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Record the start time
        startTime = SystemClock.elapsedRealtime();

        // Receive the selected difficulty level
        difficulty = getIntent().getStringExtra("difficulty");

        // Query the database for questions based on the selected difficulty level
        QuizDbHelper dbHelper = new QuizDbHelper(this);

        // Check if questions are already loaded into the database
        boolean questionsAlreadyLoaded = dbHelper.areQuestionsLoaded();

        // If questions are not already loaded, insert them into the database
        if (!questionsAlreadyLoaded) {
            // Create questions
            Question q1 = new Question("What is phishing?", "A type of fish", "A security attack to steal info", "A software program", "A data backup process", 2, "Easy");
            Question q2 = new Question("Which of these is a common sign of phishing?", "An official-looking email address", "No spelling errors", "Urgent action required", "All options are correct", 3, "Easy");
            Question q3 = new Question("What should you do if you receive a suspicious email?", "Click on the links to verify", "Forward it to your friends", "Delete it immediately", "Report it to your IT department", 4, "Medium");
            Question q4 = new Question("How can one protect against social engineering attacks?", "Use simple passwords", "Share passwords", "Regular security training", "Ignore security updates", 3, "Medium");
            Question q5 = new Question("Which type of social engineering attack involves a fake persona?", "Phishing", "Pretexting", "Baiting", "Quid pro quo", 2, "Medium");
            Question q6 = new Question("What is 'tailgating' in security terms?", "Following someone closely to enter a secure area", "Driving too closely behind another vehicle", "Sending spam emails", "Illegal streaming of sports events", 1, "Easy");
            Question q7 = new Question("What is baiting in social engineering?", "Setting up a phishing website", "Offering something tempting to steal information", "Demanding ransom via software", "Sending fake invoices", 2, "Medium");
            Question q8 = new Question("What does a quid pro quo attack involve?", "Offering free software for personal details", "Sending large number of emails", "Stealing physical security badges", "Hacking through firewall", 1, "Medium");
            Question q9 = new Question("Which practice reduces the risk of social engineering attacks?", "Using the same password everywhere", "Sharing personal information on social media", "Regularly updating software", "None of the above", 3, "Easy");
            Question q10 = new Question("What is vishing?", "A type of fishing technique", "A vocal impersonation scam", "A virus attack on mobile phones", "A virtual reality game", 2, "Medium");
            Question q11 = new Question("How should sensitive information be shared over email?", "Without encryption", "As plain text", "Using secure encryption", "On public forums", 3, "Medium");
            Question q12 = new Question("What action should you take if you suspect you are being targeted by a social engineer?", "Engage with the person to gather evidence", "Ignore all communications", "Report the incident to security personnel", "All of the above", 3, "Medium");
            Question q13 = new Question("What is a common tactic used in pretexting?", "Creating a false scenario to obtain information", "Sending spam emails", "Offering free software downloads", "Requesting money transfer", 1, "Medium");
            Question q14 = new Question("Which of the following is NOT a type of social engineering?", "Spear phishing", "Ransomware", "Baiting", "Pretexting", 2, "Easy");
            Question q15 = new Question("What is the best way to verify the identity of a caller claiming to be from tech support?", "Ask for their phone number", "Call back using an official number", "Provide them with your password", "None of the above", 2, "Medium");
            Question q16 = new Question("What is the purpose of a security awareness training program?", "To meet compliance requirements only", "To improve fishing skills", "To educate employees about security threats", "To install security software", 3, "Easy");
            Question q17 = new Question("What should you do if you find an unknown USB drive at work?", "Use it to transfer your personal files", "Ignore it", "Report it to the IT department", "Plug it into your computer to see its contents", 3, "Medium");
            Question q18 = new Question("How does 'piggybacking' differ from 'tailgating' in security breaches?", "It does not differ; they are the same", "Piggybacking is always intentional, tailgating is not", "Piggybacking requires electronic access, tailgating does not", "Tailgating involves physical access, piggybacking can be electronic", 4, "Medium");
            Question q19 = new Question("Which of these should you NOT do when creating passwords?", "Use a combination of letters, numbers, and symbols", "Use easily guessed passwords like '12345'", "Change your passwords regularly", "Use different passwords for different accounts", 2, "Easy");
            Question q20 = new Question("What tactic involves an attacker impersonating upper management?", "Vishing", "Spear phishing", "CEO fraud", "Baiting", 3, "Medium");
            Question q21 = new Question("Which of these is a best practice when using social media to prevent social engineering attacks?", "Accept all friend requests to appear friendly", "Share your location at all times", "Limit the amount of personal information you share", "Post detailed daily activities regularly", 3, "Easy");
            Question q22 = new Question("What kind of attack uses a fake but enticing offer to gather personal information?", "Phishing", "Baiting", "Vishing", "Spear phishing", 2, "Medium");
            Question q23 = new Question("In a business context, what is the most secure way to handle sensitive information?", "Discuss it openly in emails", "Use coded language in conversations", "Handle all sensitive information in secure channels", "Post it on bulletin boards", 3, "Medium");
            Question q24 = new Question("What should be your first action if you receive an email from a source asking for sensitive information?", "Provide the information if the email looks official", "Verify the source by calling the official number", "Send the information if they use technical terms", "Ignore the email completely", 2, "Medium");
            Question q25 = new Question("Which method involves sending emails to a specific group of people with tailored content?", "Phishing", "Whaling", "Spear phishing", "Scareware", 3, "Medium");
            Question q26 = new Question("How often should security awareness training be conducted to ensure effectiveness?", "Once in a lifetime", "Every year", "As needed", "Never", 2, "Easy");
            Question q27 = new Question("What is 'dumpster diving' in the context of social engineering?", "Looking for physical trash that contains valuable information", "Diving for underwater equipment", "A type of computer virus", "Illegal waste disposal methods", 1, "Medium");
            Question q28 = new Question("Which scenario exemplifies a 'quid pro quo' attack?", "An email offering free tickets in exchange for company login details", "Receiving an award for employee of the month", "An announcement of company bonuses", "A notification about IT system maintenance", 1, "Medium");
            Question q29 = new Question("What is a 'honey trap' in the context of social engineering?", "A method to catch bees", "An attractive fake offer to gather confidential information", "A type of computer bug", "A security tool to detect hackers", 2, "Medium");
            Question q30 = new Question("Which is a recommended response if contacted by a suspected social engineer over the phone?", "Hang up immediately", "Engage them to learn their techniques", "Try to sell them something in return", "Provide them with fake information to waste their time", 1, "Easy");
            Question q31 = new Question("What is the first step in a social engineering attack?", "Gathering information", "Sending an email", "Making a phone call", "Deploying malware", 1, "Medium");
            Question q32 = new Question("What does 'shoulder surfing' involve?", "Surfing the internet for security tips", "Watching someone enter sensitive information", "Sending phishing emails from close proximity", "Listening to conversations about sensitive topics", 2, "Easy");
            Question q33 = new Question("Why is it risky to post too much personal information on social media?", "It can be used to answer security questions", "It uses up your data allowance", "It is not risky", "Social media sites discourage sharing personal info", 1, "Medium");
            Question q34 = new Question("What should you verify before providing sensitive information over the phone?", "The caller's identity through known channels", "The weather conditions", "The stock market status", "The latest sports scores", 1, "Medium");
            Question q35 = new Question("What action minimizes the risk of social engineering via email?", "Opening all attachments to verify contents", "Using a single password for all accounts", "Implementing two-factor authentication", "Sharing passwords via email", 3, "Easy");
            Question q36 = new Question("What is the best practice when you receive a suspicious link?", "Click to verify its destination", "Forward it to your contacts", "Delete the message", "Analyze the link with security software", 4, "Medium");
            Question q37 = new Question("What kind of social engineering attack uses scare tactics?", "Pretexting", "Baiting", "Scareware", "Vishing", 3, "Medium");
            Question q38 = new Question("Why is regular security training important?", "It ensures compliance with industry regulations", "It is not necessary", "It only benefits the IT department", "It helps employees recognize and prevent attacks", 4, "Easy");
            Question q39 = new Question("What is impersonation in the context of social engineering?", "Mimicking a legitimate user to gain access", "Copying someone's signature", "Impersonating a celebrity on social media", "None of the above", 1, "Medium");
            Question q40 = new Question("What is a common target of social engineering attacks?", "Publicly available data", "Encrypted data", "Employee knowledge and behavior", "Corporate advertisements", 3, "Easy");
            Question q41 = new Question("Which document should be regularly updated to prevent social engineering?", "Employee handbook", "Privacy policy", "Security protocols", "All of the above", 4, "Medium");
            Question q42 = new Question("What feature should you use to secure sensitive PDF files?", "Password protection", "Colorful fonts", "Multiple hyperlinks", "Extended margins", 1, "Easy");
            Question q43 = new Question("What is a safe practice when handling emails from unknown senders?", "Reply to confirm sender's identity", "Open all attachments for inspection", "Verify links by clicking them", "Do not interact without verifying the sender", 4, "Medium");
            Question q44 = new Question("Which scenario is an example of pretexting?", "An attacker pretends to need information for a report", "A user downloads a free software to watch movies", "An email claims you have won a lottery", "A call from someone claiming to be from your bank", 1, "Medium");
            Question q45 = new Question("How should you treat links in emails from unknown sources?", "Ignore them", "Test them on isolated systems", "Open them to check content", "Send them to IT for verification", 1, "Easy");
            Question q46 = new Question("What role does 'context' play in a social engineering attack?", "It is irrelevant", "It defines how the attacker frames their pretext", "It is only important in physical attacks", "It helps the attacker choose technical tools", 2, "Medium");
            Question q47 = new Question("What is the best countermeasure against pretexting?", "Trust no one", "Verify all unsolicited requests for information", "Ignore emails and calls", "Use complex passwords", 2, "Medium");
            Question q48 = new Question("Which is NOT a characteristic of phishing emails?", "They often contain urgent requests for information", "They come from trusted sources", "They contain grammatical errors", "They request sensitive information", 2, "Easy");
            Question q49 = new Question("How do social engineers manipulate their targets?", "By offering technical support", "By creating a sense of urgency", "By providing factual information", "By keeping targets calm and collected", 2, "Medium");
            Question q50 = new Question("Which technique is effective in verifying a suspicious message?", "Asking a friend", "Contacting the supposed sender through official channels", "Ignoring the message", "Deleting the message", 2, "Easy");
            Question q51 = new Question("What is the risk of 'out-of-band' verification in dealing with potential social engineering?", "It is too time-consuming", "It is not secure", "There is no risk; it is a recommended method", "It can be bypassed by sophisticated attackers", 3, "Medium");
            Question q52 = new Question("Which of these is a potential indicator of a social engineering attempt?", "An email from a co-worker", "A request for confidential information under strange circumstances", "A notice from IT about a scheduled maintenance", "A standard operating procedure document", 2, "Easy");
            Question q53 = new Question("What is the role of 'fear' in social engineering attacks?", "It is not used by attackers", "It compels victims to act irrationally", "It ensures compliance with policies", "It is used to reassure victims", 2, "Medium");
            Question q54 = new Question("Which of these is an effective social engineering tactic?", "Providing accurate information freely", "Asking vague, unrelated questions", "Establishing a credible backstory", "Offering services at a high price", 3, "Medium");
            Question q55 = new Question("How should organizations handle sensitive information leaks?", "Ignore them as minor issues", "Investigate and respond appropriately", "Publicize them to warn others", "Use them as case studies in training", 2, "Medium");
            Question q56 = new Question("What psychological principle do social engineers exploit by offering a small gift before asking for confidential information?", "Reciprocity", "Consistency", "Social Proof", "Authority", 1, "Hard");
            Question q57 = new Question("Which technique might a social engineer use to gather information about a company's IT infrastructure without being detected?", "Dumpster diving for discarded documents", "Cold calling as an IT auditor", "Sending a phishing email", "Attending a public company meeting", 1, "Hard");
            Question q58 = new Question("In the context of security, what is 'farming'?", "Redirecting users to malicious websites via DNS hijacking", "Using a large number of people to test security", "Gathering data from multiple sources to use in an attack", "Developing new phishing techniques", 1, "Hard");
            Question q59 = new Question("What subtle technique is used in 'watering hole' attacks?", "Targeting a specific group by poisoning websites they frequently visit", "Sending emails to company executives", "Offering free downloads to gather user data", "Conducting fake job interviews to extract information", 1, "Hard");
            Question q60 = new Question("Which of the following is an indicator of a potential spear phishing attack?", "An email that addresses the recipient by a generic title", "An email that mimics the format of a trusted institution but has minor discrepancies", "An email that is perfectly crafted with no faults", "An email that offers a realistic business proposition", 2, "Hard");
            Question q61 = new Question("How does 'reverse social engineering' work?", "By tricking individuals into offering information by pretending to fix a problem", "By manipulating people to perform social tasks", "By reversing the roles in a typical phishing scenario", "By using psychological techniques to undo social conditioning", 1, "Hard");
            Question q62 = new Question("What makes 'whaling' different from typical phishing?", "Its targets are regular employees", "Its targets are high-level executives", "It does not use email as a medium", "It is not financially motivated", 2, "Hard");
            Question q63 = new Question("What behavioral trait do attackers exploit when they impersonate police or other authority figures?", "Curiosity", "Fear of authority", "Desire to help", "Loyalty to colleagues", 2, "Hard");
            Question q64 = new Question("Which data-gathering technique involves analyzing metadata from corporate websites and emails?", "Content scraping", "Phishing", "Data mining", "Open-source intelligence (OSINT)", 4, "Hard");
            Question q65 = new Question("How should an organization respond to the accidental disclosure of sensitive information?", "Publicly apologize and move on", "Conduct an internal review and strengthen policies", "Ignore the disclosure unless further incidents occur", "Only inform affected parties", 2, "Hard");
            Question q66 = new Question("Which attack involves manipulating local listings on search engines to scam users?", "SEO poisoning", "Location hijacking", "Geo-farming", "Link farming", 2, "Hard");
            Question q67 = new Question("What is the risk of embedding private URLs in public GitHub repositories?", "They can be indexed by search engines and discovered by malicious actors", "They will be automatically encrypted by GitHub", "There is no risk as GitHub is secure", "They enhance the repository's SEO", 1, "Hard");
            Question q68 = new Question("Which of these is a sophisticated physical security breach technique?", "Tailgating", "Lockpicking", "Jamming security cameras", "All of the above", 4, "Hard");
            Question q69 = new Question("What long-term strategy do advanced persistent threats (APTs) use?", "Quick strikes to evade detection", "Slow, stealthy data extraction over time", "Massive data breaches for immediate impact", "Public defacement of websites", 2, "Hard");
            Question q70 = new Question("What is 'rubber ducky' in the context of cybersecurity?", "A tool that looks like a USB drive but types pre-defined scripts when connected to a PC", "A software program that detects network intrusions", "A decoy system used to attract hackers", "A firewall feature", 1, "Hard");
            Question q71 = new Question("Identify the social engineering technique that involves attackers blending in with normal workflows to avoid detection.", "Pretexting", "Mimicry", "Baiting", "The Normalization of Deviance", 4, "Hard");
            Question q72 = new Question("Which method involves attackers using fake caller ID displays to deceive victims?", "Vishing", "ID spoofing", "Smishing", "Pretexting", 2, "Hard");
            Question q73 = new Question("What type of attack targets the mobile phones of victims using deceptive text messages?", "Phishing", "Vishing", "Smishing", "Whaling", 3, "Hard");
            Question q74 = new Question("What defense mechanism involves creating decoy networks to confuse potential hackers?", "Honeypots", "Trapdoors", "Security by obscurity", "Encryption", 1, "Hard");
            Question q75 = new Question("How do cybercriminals use 'deepfakes' in social engineering attacks?", "To impersonate corporate executives in video calls", "To enhance the graphics of phishing websites", "To solve CAPTCHAs automatically", "To encrypt sensitive information", 1, "Hard");

            // Add questions to the database
            dbHelper.addQuestion(q1);
            dbHelper.addQuestion(q2);
            dbHelper.addQuestion(q3);
            dbHelper.addQuestion(q4);
            dbHelper.addQuestion(q5);
            dbHelper.addQuestion(q6);
            dbHelper.addQuestion(q7);
            dbHelper.addQuestion(q8);
            dbHelper.addQuestion(q9);
            dbHelper.addQuestion(q10);
            dbHelper.addQuestion(q11);
            dbHelper.addQuestion(q12);
            dbHelper.addQuestion(q13);
            dbHelper.addQuestion(q14);
            dbHelper.addQuestion(q15);
            dbHelper.addQuestion(q16);
            dbHelper.addQuestion(q17);
            dbHelper.addQuestion(q18);
            dbHelper.addQuestion(q19);
            dbHelper.addQuestion(q20);
            dbHelper.addQuestion(q21);
            dbHelper.addQuestion(q22);
            dbHelper.addQuestion(q23);
            dbHelper.addQuestion(q24);
            dbHelper.addQuestion(q25);
            dbHelper.addQuestion(q26);
            dbHelper.addQuestion(q27);
            dbHelper.addQuestion(q28);
            dbHelper.addQuestion(q29);
            dbHelper.addQuestion(q30);
            dbHelper.addQuestion(q31);
            dbHelper.addQuestion(q32);
            dbHelper.addQuestion(q33);
            dbHelper.addQuestion(q34);
            dbHelper.addQuestion(q35);
            dbHelper.addQuestion(q36);
            dbHelper.addQuestion(q37);
            dbHelper.addQuestion(q38);
            dbHelper.addQuestion(q39);
            dbHelper.addQuestion(q40);
            dbHelper.addQuestion(q41);
            dbHelper.addQuestion(q42);
            dbHelper.addQuestion(q43);
            dbHelper.addQuestion(q44);
            dbHelper.addQuestion(q45);
            dbHelper.addQuestion(q46);
            dbHelper.addQuestion(q47);
            dbHelper.addQuestion(q48);
            dbHelper.addQuestion(q49);
            dbHelper.addQuestion(q50);
            dbHelper.addQuestion(q51);
            dbHelper.addQuestion(q52);
            dbHelper.addQuestion(q53);
            dbHelper.addQuestion(q54);
            dbHelper.addQuestion(q55);
            dbHelper.addQuestion(q56);
            dbHelper.addQuestion(q57);
            dbHelper.addQuestion(q58);
            dbHelper.addQuestion(q59);
            dbHelper.addQuestion(q60);
            dbHelper.addQuestion(q61);
            dbHelper.addQuestion(q62);
            dbHelper.addQuestion(q63);
            dbHelper.addQuestion(q64);
            dbHelper.addQuestion(q65);
            dbHelper.addQuestion(q66);
            dbHelper.addQuestion(q67);
            dbHelper.addQuestion(q68);
            dbHelper.addQuestion(q69);
            dbHelper.addQuestion(q70);
            dbHelper.addQuestion(q71);
            dbHelper.addQuestion(q72);
            dbHelper.addQuestion(q73);
            dbHelper.addQuestion(q74);
            dbHelper.addQuestion(q75);

            // Mark questions as loaded in the database
            dbHelper.markQuestionsAsLoaded();
        }

        // Retrieve questions from the database based on the selected difficulty level
        questionList = dbHelper.getQuestionsByDifficulty(difficulty);

        // Ensure that the question list is not null and contains questions
        if (questionList == null || questionList.isEmpty()) {
            // Handle the case where no questions are retrieved from the database
            // You might want to display an error message or take appropriate action
            Log.e("QuizActivity", "No questions retrieved from the database");
            finish(); // Finish the activity
            return;
        }

        // Initialize UI components
        questionTextView = findViewById(R.id.questionTextView);
        option1Button = findViewById(R.id.option1Button);
        option2Button = findViewById(R.id.option2Button);
        option3Button = findViewById(R.id.option3Button);
        option4Button = findViewById(R.id.option4Button);

        // Display the first question
        showQuestion();

        // Set click listeners for answer buttons
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(1);
            }
        });

        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(2);
            }
        });

        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(3);
            }
        });

        option4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(4);
            }
        });
    }


    private void showQuestion() {
            Question question = questionList.get(currentQuestionIndex);
            questionTextView.setText(question.getQuestion());
            option1Button.setText(question.getOption1());
            option2Button.setText(question.getOption2());
            option3Button.setText(question.getOption3());
            option4Button.setText(question.getOption4());
    }

    private void checkAnswer(int selectedOption) {
        Log.d("QuizActivity", "checkAnswer: currentQuestionIndex = " + currentQuestionIndex);
        Log.d("QuizActivity", "checkAnswer: questionList size = " + questionList.size());

        if (currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            if (selectedOption == question.getAnswerNr()) {
                // Correct answer
                score++;
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            } else {
                // Incorrect answer
                Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
            }
            currentQuestionIndex++;

            if (currentQuestionIndex < questionList.size()) {
                // Display the next question
                showQuestion();
            } else {
                endQuiz();
            }
        } else {
            // Handle the case where currentQuestionIndex exceeds the questionList size
            // This could occur if the user taps too quickly or if there's a concurrency issue
            // You might want to log an error or handle it gracefully
            // For now, we'll just finish the quiz
            Log.e("QuizActivity", "checkAnswer: currentQuestionIndex exceeded questionList size");
            endQuiz();
        }
    }


    private void endQuiz() {
        // Calculate the time taken
        long elapsedTime = SystemClock.elapsedRealtime() - startTime;
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds %= 60;
        String timeTaken = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


        // Display the user's final score and time taken
        Toast.makeText(this, "Quiz ended. Your score: " + score + "\nTime taken: " + timeTaken, Toast.LENGTH_LONG).show();

        // Store the score and time taken in the database
        ScoresDbHelper dbHelper = new ScoresDbHelper(this);
        dbHelper.addQuizResult(score, timeTaken, difficulty);
        // Finish the activity
        finish();
    }

}
