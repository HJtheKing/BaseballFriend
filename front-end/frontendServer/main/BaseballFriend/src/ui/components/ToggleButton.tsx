interface ToggleButtonProps {
  value: boolean;
  onChange: (value: boolean) => void;
}

const ToggleButton = ({ value, onChange }: ToggleButtonProps) => {
  const handleClick = () => {
    onChange(!value);
  };
  return (
    <div className="h-[50px] flex justify-self-center align-middle my-auto">
      <div
        onClick={handleClick}
        className={`toggle-container ${value ? 'on' : 'off'}`}
      >
        <div className="toggle-button"></div>
      </div>
    </div>
  );
};

export default ToggleButton;
