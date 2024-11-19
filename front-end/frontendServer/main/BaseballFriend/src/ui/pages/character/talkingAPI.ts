import OpenAI from 'openai';

const openai = new OpenAI({
  apiKey: import.meta.env.VITE_OPENAI_API_KEY, // 실제 API 키로 교체하세요
  dangerouslyAllowBrowser: true, // 브라우저에서 바로 api 사용
});

export async function getChatCompletion(talk: string) {
  try {
    const completion = await openai.chat.completions.create({
      model: 'gpt-4',
      messages: [
        {
          role: 'system',
          content:
            '너는 야구를 함께 즐기는 고양이 친구야. 너가 말을 할 때 끝에 "냥"을 항상 붙여줘. 친구랑 대화하듯 답해줘',
        },
        {
          role: 'user',
          content: talk,
        },
      ],
    });
    const responseMessage = completion.choices[0].message.content;
    return responseMessage;
  } catch (error) {
    console.error('Error:', error);
  }
}
