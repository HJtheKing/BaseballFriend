import { useState } from 'react';
import useCharacterStore from '../../store/useCharacterStore';
import '../../css/scrollbar.css';
import { getChatCompletion } from './talkingAPI';
import ball from '../../assets/imgs/Ball.png';
export const talkingSize: { width: number; height: number } = {
  width: 400,
  height: 500,
};

const TalkingComponent = () => {
  const { setIgnoreMouseEvent, talkingPos, talking } = useCharacterStore();
  const [myText, setMyText] = useState<string>('');
  const [messages, setMessages] = useState<
    { type: 'request' | 'response'; text: string; loading?: boolean }[]
  >([]);

  if (!talking) return null;

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (myText.trim().length === 0) return;
    const newMessage = { type: 'request' as const, text: myText };
    setMessages((prevMessages) => [
      ...prevMessages,
      newMessage,
      { type: 'response' as const, text: '', loading: true },
    ]);
    setMyText('');

    const fetchData = async () => {
      const result = await getChatCompletion(myText);
      setMessages((prevMessages) =>
        prevMessages.map((msg, index) =>
          index === prevMessages.length - 1
            ? { ...msg, text: String(result), loading: false }
            : msg,
        ),
      );
    };
    fetchData();
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMyText(e.target.value);
  };

  return (
    <div
      onMouseEnter={() => setIgnoreMouseEvent(false)}
      onMouseLeave={() => setIgnoreMouseEvent(true)}
      className="scrollbar bg-white absolute"
      style={{
        boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
        width: `${talkingSize.width}px`,
        height: `${talkingSize.height}px`,
        left: talkingPos?.x,
        top: talkingPos?.y,
        zIndex: 1000,
      }}
    >
      <div className="flex flex-col h-full">
        <div
          className="flex-1 overflow-auto p-4"
          style={{ paddingBottom: '60px' }}
        >
          {messages.map((message, index) => (
            <div
              key={index}
              className={`mb-2 p-2 flex ${
                message.type === 'request' ? 'justify-end' : 'justify-start'
              }`}
            >
              <div
                className={`Jua p-2 rounded-lg ${
                  message.type === 'request'
                    ? 'bg-[#545454] text-white'
                    : 'bg-[#EEEEEE] text-black'
                }`}
                style={{
                  maxWidth: '90%',
                  boxShadow: '2px 2px 5px rgba(0, 0, 0, 0.3)',
                }}
              >
                <span>
                  {message.type === 'request' ? (
                    message.text
                  ) : message.loading ? (
                    <div className="h-[30px] w-[30px] transition-transform rotate-180">
                      <img
                        className=" w-full h-full infi-rotate"
                        src={ball}
                        alt=""
                      />
                    </div>
                  ) : (
                    message.text
                  )}
                </span>
              </div>
            </div>
          ))}
        </div>
        <form
          onSubmit={handleSubmit}
          className="flex items-center justify-center p-4 border-t bg-[#EEEEEE]"
        >
          <input
            type="text"
            value={myText}
            className="Jua shadow-xl h-9 w-80 text-black border-[1px] border-gray-400 bg-white rounded-md text-center focus:outline-none text-base"
            placeholder="안녕~"
            onChange={handleInputChange}
          />
          <button type="submit">
            <img
              src={ball}
              alt="ball"
              className="h-10 w-10 ml-3 transform transition-transform duration-200 hover:scale-125"
            />
          </button>
        </form>
      </div>
    </div>
  );
};

export default TalkingComponent;
