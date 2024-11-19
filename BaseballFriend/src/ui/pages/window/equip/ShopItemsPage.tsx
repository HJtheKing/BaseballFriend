// import { useState } from 'react';
import { useEffect, useState } from 'react';
import { BuyChr, BuyItem, EquipmentInfo, ShopitemsInfo } from './api';
import { ReactComponent as ItemDefault } from './ItemDefault.svg';
import { ShopItems } from './equipType';
import Swal from 'sweetalert2';

const ShopItemsPage = ({ ...props }) => {
  const [chrList, setChrlist] = useState<ShopItems['characterList']>([
    {
      characterSerialNumber: 0,
      characterName: '배트냥이',
    },
  ]);
  const [itemList, setItemlist] = useState<
    [
      {
        itemSerialNumber: number;
        characterSerialNumber: number;
        itemCategory: number;
        price: number;
        teamName: String;
      },
    ]
  >();
  const [bgList, setBglist] = useState<ShopItems['backgroundList']>(null);
  // const myItems = props.chrInfo;
  const myMoney = props.gameMoney;
  const setMymoney = props.setGamemoney;
  const chrId = props.chrIndex;
  const arms = ['Bat0', 'Bat1', 'Arm2', 'Arm3'];
  const bodys = ['Bag0', 'Cape0', 'Cape1'];
  const heads = ['Cap0', 'Cap1', 'Cap2', 'Cap3'];
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

  const fetchData = async () => {
    const data = (await ShopitemsInfo()) as ShopItems;
    setChrlist(data.characterList);
    setItemlist(data.itemList);
    setBglist(data.backgroundList);
  };

  const buySuccess = () => {
    Swal.fire({
      title: '구매 완료!',
      text: '구매가 완료되었습니다!',
      icon: 'success',
      width: '80%',
    });
    fetchData();
  };

  const buyFail = () => {
    Swal.fire({
      title: '구매 실패!',
      text: '잔액이 부족합니다!',
      icon: 'error',
      width: '80%',
    });
  };

  const buyError = () => {
    Swal.fire({
      title: '구매 실패!',
      text: '통신 에러!',
      icon: 'error',
      width: '80%',
    });
  };

  const buyChr = async (serialNumber: number, money: number) => {
    if (myMoney >= money) {
      try {
        const data = await BuyChr(serialNumber);
        if (data) {
          buySuccess();
          setMymoney(myMoney - money);
          const result = await EquipmentInfo();
          //@ts-ignore
          await window.electron.setStoreValue('equips', result);
        }
      } catch {
        buyError();
      }
    } else buyFail();
  };

  const buyHead = async (serialNumber: number, money: number) => {
    console.log(serialNumber);
    if (myMoney >= money) {
      try {
        const data = await BuyItem(serialNumber);
        if (data) {
          buySuccess();
          setMymoney(myMoney - money);
          const result = await EquipmentInfo();
          //@ts-ignore
          await window.electron.setStoreValue('equips', result);
        }
      } catch (err) {
        console.log(err);
        buyError();
      }
    } else buyFail();
  };

  const renderChr = (
    list: [{ characterSerialNumber: number; characterName: string }],
  ) => {
    const temp = [];
    for (const item of list) {
      temp.push(
        <div
          key={item.characterSerialNumber}
          style={{
            width: '150px',
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'center',
            textAlign: 'center',
            border: '1px',
            flexShrink: 0,
          }}
        >
          <img
            src={`/items/${chrs[item.characterSerialNumber]}.png`}
            style={{ cursor: 'pointer' }}
          />
          <ItemDefault style={{ cursor: 'pointer' }} />
          <h2>1500볼</h2>
          <button
            onClick={() => {
              buyChr(item.characterSerialNumber, 1500);
            }}
          >
            구매
          </button>
        </div>,
      );
    }
    return temp;
  };

  const renderItems = (
    list: [
      {
        itemSerialNumber: number;
        characterSerialNumber: number;
        itemCategory: number;
        price: number;
        teamName: String;
      },
    ],
    category: number,
  ) => {
    const temp = [];
    for (const item of list) {
      if (
        category === 0 &&
        category === item.itemCategory &&
        chrId === item.characterSerialNumber
      ) {
        if ((item.itemSerialNumber / 10) % 2 < 1) {
          temp.push(
            <div
              key={item.itemSerialNumber}
              style={{
                width: '150px',
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                textAlign: 'center',
                border: '1px',
                flexShrink: 0,
              }}
            >
              <img
                src={`/items/${item.teamName}.png`}
                style={{ cursor: 'pointer' }}
              />
              <ItemDefault style={{ cursor: 'pointer' }} />
              <h2>500볼</h2>
              <button
                onClick={() => {
                  buyHead(item.itemSerialNumber, 500);
                }}
              >
                구매
              </button>
            </div>,
          );
        } else {
          temp.push(
            <div
              key={item.itemSerialNumber}
              style={{
                width: '150px',
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                textAlign: 'center',
                border: '1px',
                flexShrink: 0,
              }}
            >
              <img
                src={`/items/${heads[item.itemSerialNumber % 10]}.png`}
                style={{ cursor: 'pointer' }}
              />
              <ItemDefault style={{ cursor: 'pointer' }} />
              <h2>500볼</h2>
              <button
                onClick={() => {
                  buyHead(item.itemSerialNumber, 500);
                }}
              >
                구매
              </button>
            </div>,
          );
        }
      } else if (
        category !== 0 &&
        category === item.itemCategory &&
        chrId === item.characterSerialNumber
      ) {
        temp.push(
          <div
            key={item.itemSerialNumber}
            style={{
              width: '150px',
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'center',
              textAlign: 'center',
              border: '1px',
              flexShrink: 0,
            }}
          >
            <img
              src={`/items/${
                category === 1
                  ? bodys[item.itemSerialNumber % 10]
                  : arms[item.itemSerialNumber % 10]
              }.png`}
              style={{ cursor: 'pointer' }}
            />
            <ItemDefault style={{ cursor: 'pointer' }} />
            {category === 1 ? <h2>250볼</h2> : <h2>400볼</h2>}
            <button
              onClick={() => {
                buyHead(item.itemSerialNumber, category === 1 ? 250 : 400);
              }}
            >
              구매
            </button>
          </div>,
        );
      }
    }
    return temp;
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div
      style={{ height: '80vh' }}
      className="scrollbar"
    >
      <div style={{ padding: '20px' }}>
        {/* <div>
          <h1>캐릭터</h1>
          <div
            style={{
              display: 'flex',
              width: '100%',
              height: '100%',
            }}
            className="scrollbarx"
          >
            {renderChr(chrList)}
          </div>
        </div> */}
        <div>
          <h1>머리</h1>
          <div
            style={{
              display: 'flex',
              width: '100%',
              height: '100%',
            }}
            className="scrollbarx"
          >
            {itemList && renderItems(itemList, 0)}
          </div>
        </div>
        <div>
          <h1>몸통</h1>
          <div
            style={{
              display: 'flex',
              width: '100%',
              height: '100%',
            }}
            className="scrollbarx"
          >
            {itemList && renderItems(itemList, 1)}
          </div>
        </div>
        <div>
          <h1>팔</h1>
          <div
            style={{
              display: 'flex',
              width: '100%',
              height: '100%',
            }}
            className="scrollbarx"
          >
            {itemList && renderItems(itemList, 2)}
          </div>
        </div>
        {/* <div>
          <h1>배경</h1>
          <div
            style={{
              display: 'flex',
              width: '100%',
              height: '100%',
            }}
            className="scrollbarx"
          >
            {renderChr(chrList)}
          </div>
        </div> */}
      </div>
    </div>
  );
};

export default ShopItemsPage;
