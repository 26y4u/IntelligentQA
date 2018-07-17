package com.example.Utils;


import com.example.SqsxQuestion.*;
import com.example.SqsxUser.SqsxUser;
import com.example.SqsxUser.SqsxUserRepository;
import com.qiniu.util.Json;
import com.sun.scenario.effect.impl.prism.ps.PPSBlend_SRC_OUTPeer;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
@RestController
public class ImportJson {
    SqsxQuestionRepository questionRepository;
    SqsxAnswerRepository answerRepository;
    SqsxUserRepository userRepository;
    SqsxQuestionTagRepository questionTagRepository;

    public ImportJson(SqsxQuestionRepository questionRepository, SqsxAnswerRepository answerRepository, SqsxUserRepository userRepository, SqsxQuestionTagRepository questionTagRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.questionTagRepository = questionTagRepository;
    }


    public JSONArray testAdd() {

        String path = "C:\\Users\\Administrator\\Desktop\\data4.json";
        JSONArray jsonArray = null;
        try {
            String input = FileUtils.readFileToString(new File(path), "UTF-8");
            jsonArray = new JSONArray(input.substring(1));

        } catch (Exception e) {
            e.printStackTrace();
            jsonArray = null;
        }
        SqsxUser user1 = new SqsxUser();
        user1.setId(9998);
        user1.setUsername("不小于八个字符" + 10000);
        user1.setPassword(ToHash.toHash("buxiaoyubagezifu" + 10000));
        user1.setType(1);
        user1.setIsdel(0);
        userRepository.save(user1);

        SqsxUser user0 = new SqsxUser();
        user0.setId(9999);
        user0.setUsername("不小于八个字符" + 20000);
        user0.setPassword(ToHash.toHash("buxiaoyubagezifu" + 20000));
        user0.setType(1);
        user0.setIsdel(0);
        userRepository.save(user0);
        int answer_id = 20000;
        for (int i = 0; i < jsonArray.length(); i++) {
            System.out.println(i);
            JSONObject object = (JSONObject) jsonArray.get(i);
            String title = object.getString("title").trim();
            String desc = object.getString("desc").trim();

            String answer_best = object.getString("answer_best").trim();
            JSONArray tags = (JSONArray) object.get("tags");
            JSONArray answers_other = (JSONArray) object.get("answers_other");
            SqsxUser user = new SqsxUser();

            user.setId(10000 + i);
            user.setUsername("不小于八个字符" + i);
            user.setPassword(ToHash.toHash("buxiaoyubagezifu" + i));
            user.setType(1);
            user.setIsdel(0);
            userRepository.save(user);

            SqsxQuestionBean sqsxQuestionBean = new SqsxQuestionBean();
            sqsxQuestionBean.setDescription(desc);
            sqsxQuestionBean.setTitle(title);
            sqsxQuestionBean.setUser_id(user.getId());
            sqsxQuestionBean.setId(9999 + i);
            int answer_count = 0;
            if (answer_best == null || answer_best.equals("")) {
                answer_count = answers_other.length();

            } else {
                answer_count = answers_other.length() + 1;

                SqsxAnswerBean bestAnswer = new SqsxAnswerBean();
                bestAnswer.setDetails(answer_best);
                bestAnswer.setQuestion_id(10000 + i);
                bestAnswer.setUser_id(9999 + i);
                bestAnswer.setTime(GetTime.getTime());
                bestAnswer.setUsertype(1);
                bestAnswer.setId(10000 + i);
                try{
                    answerRepository.save(bestAnswer);
                }catch (RuntimeException e){
                    System.out.println("bestAns" + bestAnswer.getDetails()+bestAnswer.getDetails().length());
                }


                sqsxQuestionBean.setBest_answer_id(10000 + i);
            }
            sqsxQuestionBean.setCount_answer(answer_count);
            sqsxQuestionBean.setTime(GetTime.getTime());
            sqsxQuestionBean.setIsclose(0);
            sqsxQuestionBean.setIsdel(0);
            try{
                questionRepository.save(sqsxQuestionBean);
            }catch (RuntimeException e){
                System.out.println("question-des" + sqsxQuestionBean.getDescription()+sqsxQuestionBean.getDescription().length());
                System.out.println("question-title" + sqsxQuestionBean.getTitle()+sqsxQuestionBean.getTitle().length());
                return null;
            }
            for (int j = 0; j < tags.length(); j++) {
                String tag = tags.getString(j).trim();
                if(tag==null||tag.equals("")){
                    continue;
                }
                SqsxQuestionTagBean tagBean = new SqsxQuestionTagBean();
                tagBean.setLabel(tag);
                tagBean.setQuestion_id(10000 + i);
                try{
                    questionTagRepository.save(tagBean);
                }catch (RuntimeException e){
                    System.out.println(tagBean.getLabel());
                    e.printStackTrace();
                    return null;
                }

            }
            for (int j = 0; j < answers_other.length(); j++) {
                if(answers_other==null||answers_other.equals("")){
                    continue;
                }
                String ans = answers_other.getString(j).trim();
                SqsxAnswerBean answerBean = new SqsxAnswerBean();
                answerBean.setUsertype(1);
                answerBean.setTime(GetTime.getTime());
                answerBean.setUser_id(9998 + i);
                answerBean.setCount_like(0);
                answerBean.setDetails(ans);
                answerBean.setQuestion_id(10000 + i);
                answerBean.setId(answer_id++);
                try{
                    answerRepository.save(answerBean);
                }catch (RuntimeException e){
                    System.out.println("answerBean" + answerBean.getDetails()+answerBean.getDetails().length());
                    return null;
                }
            }


        }


        return jsonArray;

    }

    /**
     * 将unicode转换成utf
     *
     * @param theString
     * @return
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static String savefile = "C:\\Users\\Zhu\\Desktop\\data5.json";

    private static void saveAsFileWriter(String content) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(savefile);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}

