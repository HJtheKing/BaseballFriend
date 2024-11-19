import { useEffect, useRef, useState } from 'react';
import { Canvas } from '@react-three/fiber';
import moneyUrl from '../../../assets/imgs/Ball.png?url';
import EquipModel from './EquipModel';
import { OrbitControls } from '@react-three/drei';
import { OrbitControls as OrbitControlsImpl } from 'three-stdlib';
import '../../../css/Dynamic_button.css';
import ItemsList from './ItemsListPage';
import '../../../css/character.css';
import Swal from 'sweetalert2';
import {
  EquipmentInfo,
  GetmoneyData,
  GetMyteam,
  SubmitEquipitems,
} from './api';
import { EquipInfo } from './equipType';
import ShopItemsPage from './ShopItemsPage';

const EquipPage = () => {
  const orbitRef = useRef<OrbitControlsImpl>(null); // orbitcontrol 타입 레퍼런스
  const [equipData, setequipData] = useState<EquipInfo | null>(null);
  const [url, setUrl] = useState('0.glb');
  const [chrIndex, setChrindex] = useState<number>(0);
  const [curEquiped, setcurEquiped] = useState([-1, -1, -1, 0, -1]);
  const [isShop, setShop] = useState(false);
  const [category, setCategory] = useState([1, 0, 0, 0, 0]); // 머리, 몸통, 팔, 캐릭터, 배경
  const [categoryIndex, setCategoryIndex] = useState(0);
  const [background, setBackground] = useState(-1);
  const [gamemoney, setGamemoney] = useState(0);
  const [team, setTeam] = useState('');
  const [items, setItems] = useState([[], [], []] as number[][]); // index 0 : 머리 1 : 몸통 2 : 팔

  const fetchData = async () => {
    // @ts-ignore
    const result = await window.electron.getStoreValue('equips');
    const myTeam = await GetMyteam();
    const getMoney = await GetmoneyData();
    setequipData(result);
    setChrindex(result.characterSerialNumber);
    setcurEquiped([
      result.headItemSerialNumber,
      result.bodyItemSerialNumber,
      result.armItemSerialNumber,
      result.characterSerialNumber,
      result.backgroundSerialNumber,
    ]);
    setBackground(result.backgroundSerialNumber);
    const glbUrl = `${chrIndex}.glb`;
    setUrl(glbUrl);
    setGamemoney(getMoney.gameMoney);
    setTeam(myTeam);
  };

  useEffect(() => {
    fetchData();
  }, [isShop]);

  useEffect(() => {
    if (!equipData) return;
    const head = [] as number[];
    const body = [] as number[];
    const arm = [] as number[];
    const curChr = equipData.characterList.filter((chr) => {
      return chr.characterSerialNumber === chrIndex;
    });
    if (!curChr.length) return;
    const chrItems = curChr[0].itemList;
    if (!chrItems) return;
    for (const i of chrItems) {
      if (i.itemCategory === 0) {
        head.push(i.itemSerialNumber);
      } else if (i.itemCategory === 1) {
        body.push(i.itemSerialNumber);
      } else if (i.itemCategory === 2) {
        arm.push(i.itemSerialNumber);
      }
    }
    setItems(() => {
      const temp = [head, body, arm] as number[][];
      return temp;
    });
  }, [chrIndex, equipData]);

  const submitItems = async () => {
    const items = {
      characterInfoSerialNumber: chrIndex,
      headItemSerialNumber: curEquiped[0],
      bodyItemSerialNumber: curEquiped[1],
      armItemSerialNumber: curEquiped[2],
      backgroundSerialNumber: -1,
    };
    await SubmitEquipitems(items);
    const result = await EquipmentInfo();
    //@ts-ignore
    await window.electron.setStoreValue('equips', result);
    fetchData();
  };

  return (
    <div style={{ width: '95%', height: '100%', overflowY: 'hidden' }}>
      <div
        className="up-bar"
        style={{
          marginTop: '1rem',
          width: '100%',
          height: '10%',
          display: 'flex',
          justifyContent: 'space-between',
        }}
      >
        <div
          style={{
            background: '#FFFFFF',
            borderRadius: '5%',
            width: '150px',
            height: '100%',
            display: 'flex',
            flexDirection: 'row',
            alignItems: 'center',
          }}
        >
          <>
            <button
              className={!isShop ? 'select-button on' : 'select-button'}
              onClick={() => {
                setShop(false);
                if (orbitRef.current) {
                  orbitRef.current.reset();
                }
              }}
            >
              <h1>옷장</h1>
            </button>
          </>
          <>
            <button
              className={isShop ? 'select-button on' : 'select-button'}
              onClick={() => {
                setShop(true);
                if (orbitRef.current) {
                  orbitRef.current.reset();
                }
              }}
            >
              <h1>상점</h1>
            </button>
          </>
        </div>

        <div
          style={{
            display: 'flex',
            justifyContent: 'space-around',
            alignItems: 'center',
          }}
        >
          <img
            style={{ width: '50px' }}
            src={moneyUrl}
            alt="게임머니"
          />
          <h1>{gamemoney}</h1>
        </div>
      </div>
      <div className={isShop ? 'shop-center' : 'equip-center'}>
        {!isShop && (
          <div
            style={{
              marginTop: '1rem',
              background: '#FFF5CA',
              borderRadius: '5%',
              width: '75px',
              height: '275px', // 높이 증가
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <button
              className={category[0] ? 'select-button on' : 'select-button'}
              onClick={() => {
                if (!category[0]) {
                  setCategory([1, 0, 0, 0, 0]);
                  setCategoryIndex(0);
                }
              }}
            >
              <h1>머리</h1>
            </button>
            <button
              className={category[1] ? 'select-button on' : 'select-button'}
              onClick={() => {
                if (!category[1]) {
                  setCategory([0, 1, 0, 0, 0]);
                  setCategoryIndex(1);
                }
              }}
            >
              <h1>몸통</h1>
            </button>
            <button
              className={category[2] ? 'select-button on' : 'select-button'}
              onClick={() => {
                if (!category[2]) {
                  setCategory([0, 0, 1, 0, 0]);
                  setCategoryIndex(2);
                }
              }}
            >
              <h1>팔</h1>
            </button>
            {/* <button
              className={category[3] ? 'select-button on' : 'select-button'}
              onClick={() => {
                if (!category[3]) {
                  setCategory([0, 0, 0, 1, 0]);
                  setCategoryIndex(3);
                }
              }}
            >
              <h1>캐릭터</h1>
            </button> */}
            {/* <button
              className={category[4] ? 'select-button on' : 'select-button'}
              onClick={() => {
                if (!category[4]) {
                  setCategory([0, 0, 0, 0, 1]);
                  setCategoryIndex(4);
                }
              }}
            >
              <h1>배경</h1>
            </button> */}
          </div>
        )}
        <div
          className={
            isShop ? 'character-position-shop' : 'character-position-equip'
          }
        >
          <Canvas
            style={{ height: '60%' }}
            camera={{ position: [-5, 0, 10] }}
          >
            <ambientLight intensity={1.75} />
            <EquipModel
              url={url}
              head={curEquiped[0]}
              body={curEquiped[1]}
              arm={curEquiped[2]}
              background={background}
            />
            <OrbitControls
              ref={orbitRef}
              enablePan={false}
              enableZoom={false}
            />
          </Canvas>
          <h1>뱃냥이</h1>
          {!isShop && (
            <button
              className="submit-button"
              onClick={() => {
                submitItems();
                Swal.fire({
                  title: '확정!',
                  text: '저장이 완료되었습니다!',
                  icon: 'success',
                  width: '80%',
                  // heightAuto: 'true',
                });
              }}
            >
              <h1>확정</h1>
            </button>
          )}
        </div>
        {isShop && (
          <div style={{ width: '70%', height: '90%' }}>
            <ShopItemsPage
              setEquiped={setcurEquiped}
              chrInfo={equipData?.characterList}
              itemInfo={items}
              gameMoney={gamemoney}
              setGamemoney={setGamemoney}
              myTeam={team}
              chrIndex={chrIndex}
            />
          </div>
        )}
      </div>
      <>
        {!isShop && (
          <div style={{ width: '100%', height: '30%' }}>
            <h1 style={{ textAlign: 'left' }}>
              {categoryIndex === 3
                ? '캐릭터 목록'
                : categoryIndex === 4
                  ? '배경 목록'
                  : '아이템 목록'}
            </h1>
            {categoryIndex <= 2 ? (
              items[categoryIndex] ? (
                <ItemsList
                  itemList={items[categoryIndex]}
                  curEquiped={curEquiped}
                  curCategory={categoryIndex}
                  setEquiped={setcurEquiped}
                  myTeam={team}
                />
              ) : (
                <h1>현재 보유중인 악세사리가 없습니다!</h1>
              )
            ) : categoryIndex === 3 ? (
              <ItemsList
                itemList={
                  equipData?.characterList.map(
                    (char) => char.characterSerialNumber,
                  ) || []
                }
                curEquiped={curEquiped}
                curCategory={3}
                setEquiped={(newVal: number[]) => setChrindex(newVal[0])}
              />
            ) : (
              <ItemsList
                itemList={
                  equipData?.backgroundList?.map(
                    (bg) => bg.backgroundSerialNumber,
                  ) || []
                }
                curEquiped={curEquiped}
                curCategory={4}
                setEquiped={(newVal: number[]) => setBackground(newVal[0])}
              />
            )}
          </div>
        )}
      </>
    </div>
  );
};

export default EquipPage;
