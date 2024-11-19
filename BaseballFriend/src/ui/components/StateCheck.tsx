import useCharacterStore from '../store/useCharacterStore';

const StateCheck = () => {
  const {
    isDragging,
    isIgnored,
    nowAnimation,
    menubar,
    alwaysDisplay,
    talking,
    newAlarm,
    alarmAnimationId,
  } = useCharacterStore();
  return (
    <div className="absolute bg-white w-[300px]">
      <h1>상태 확인용</h1>
      <h1>menuBar : {menubar ? 'True' : 'False'}</h1>
      <h1>isDragging : {isDragging ? 'True' : 'False'}</h1>
      <h1>isIgnored : {isIgnored ? 'True' : 'False'}</h1>
      <h1>nowAnimation : {nowAnimation}</h1>
      <h1>alwaysDisplay : {alwaysDisplay ? 'True' : 'False'}</h1>
      <h1>talking : {talking ? 'True' : 'False'}</h1>
      <h1>newAlarm : {newAlarm ? 'True' : 'False'}</h1>
      <h1>alarmAnimationId : {alarmAnimationId}</h1>
    </div>
  );
};

export default StateCheck;
