import { ReactComponent as ItemDefault } from './ItemDefault.svg';
import '../../../css/scrollbar.css';

const ItemsList = ({ ...props }) => {
  const items = props.itemList;
  const setE = props.setEquiped;
  const curE = props.curEquiped;
  const category = props.curCategory;
  const arms = ['Bat0', 'Bat1', 'Arm2', 'Arm3'];
  const bodys = ['Bag0', 'Cape0', 'Cape1'];
  const heads = ['Cap0', 'Cap1', 'Cap2', 'Cap3'];
  const teams = [
    'KIA',
    'KT',
    'LG',
    'NC',
    'SSG',
    '두산',
    '롯데',
    '삼성',
    '키움',
    '한화',
  ];
  const chrs = [
    'Cat0',
    'Cat1',
    'Cat2',
    'Bear0',
    'Bear1',
    'Bear2',
    'Bird0',
    'Bird1',
    'Bird2',
  ];
  const backs = ['Back0', 'Back1'];

  const changeSelect = (item: number) => {
    setE(() => {
      const arr = [...curE];
      const chg = arr[category];
      if (chg !== item) arr[category] = item;
      else arr[category] = -1;
      return arr;
    });
  };
  const render = () => {
    const result = [];
    let name = '';
    for (const i in items) {
      if (category === 0) {
        if ((items[i] / 10) % 2 >= 1) {
          name = heads[items[i] % 10];
        } else {
          name = teams[items[i] % 10];
        }
      } else if (category === 1) {
        name = bodys[items[i] % 10];
      } else if (category === 2) {
        name = arms[items[i] % 10];
      } else if (category === 3) {
        name = chrs[items[i]];
      } else if (category === 4) {
        name = backs[items[i] % 2];
      }
      const url = `items/${name}.png`;
      result.push(
        <div
          className={curE[category] === items[i] ? 'selected-frame' : ''}
          key={i}
          style={{
            width: '150px',
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            textAlign: 'center',
            border: '1px',
          }}
        >
          <img
            src={url}
            alt={`${items[i]}`}
            style={{ cursor: 'pointer' }}
            onClick={() => {
              changeSelect(items[i]);
            }}
          />
          <ItemDefault
            style={{ cursor: 'pointer' }}
            onClick={() => {
              changeSelect(items[i]);
            }}
          />
        </div>,
      );
    }
    return result;
  };
  return (
    <div
      className="scrollbar"
      style={{
        overflowY: 'scroll',
        height: '100%',
        display: 'flex',
        flexWrap: 'wrap',
        justifyContent: 'space-around',
        alignItems: 'center',
      }}
    >
      {render()}
    </div>
  );
};

export default ItemsList;
