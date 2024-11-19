import useCharacterStore from '../store/useCharacterStore';

const ToggleButton2 = () => {
  const { setAlwaysDisplay, alwaysDisplay } = useCharacterStore();

  const handleChange = () => {
    setAlwaysDisplay(!alwaysDisplay);
  };
  return (
    <div className="h-[50px] flex justify-self-center align-middle my-auto">
      <div
        onClick={handleChange}
        className={`toggle-container2 ${alwaysDisplay ? 'on' : 'off'}`}
      >
        <div className="toggle-button2"></div>
      </div>
    </div>
  );
};

export default ToggleButton2;
