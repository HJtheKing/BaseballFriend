import { useEffect, useState } from 'react';

interface SelectBoxProps {
  options: string[];
  selectedValue: string | null;
  onChange: (value: string) => void;
}

const SelectBox = ({ options, selectedValue, onChange }: SelectBoxProps) => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedOption, setSelectedOption] = useState(options[0]);

  useEffect(() => {
    if (selectedValue) {
      setSelectedOption(selectedValue);
    } else {
      setSelectedOption(options[0]);
    }
  }, [selectedValue, options]);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleOptionClick = (option: string) => {
    setSelectedOption(option);
    setIsOpen(false);
    onChange(option);
  };

  return (
    <div className="select-box">
      <div
        className="select-box__selected"
        onClick={toggleDropdown}
      >
        {selectedOption}
      </div>
      {isOpen && (
        <ul className="select-box__options">
          {options.map((option, index) => (
            <li
              key={index}
              className="select-box__option"
              onClick={() => handleOptionClick(option)}
            >
              {option}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default SelectBox;
