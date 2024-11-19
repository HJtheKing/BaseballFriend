import ToggleButton2 from '../../components/ToggleButton2';
import useCharacterStore from '../../store/useCharacterStore';
import useIpcStore from '../../store/useIpcStore';

export const menuSize: { width: number; height: number } = {
  width: 130,
  height: 360,
};

const CustomDropdown = () => {
  const { menubarPos, menubar, setIgnoreMouseEvent, setMenumbar } =
    useCharacterStore();
  const { openMainWindow, openNewRealtime } = useIpcStore();
  if (!menubar) return null;

  return (
    <div
      onMouseEnter={() => setIgnoreMouseEvent(false)}
      onMouseLeave={() => setIgnoreMouseEvent(true)}
    >
      <ul
        className="rounded-[8px] bg-[#D4DFE9] absolute Jua px-[8px] py-[3px]"
        style={{
          boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
          width: `${menuSize.width}px`,
          left: menubarPos?.x,
          top: menubarPos?.y,
          zIndex: 1000,
        }}
      >
        <li
          className="ddList my-[5px]"
          onClick={() => {
            openMainWindow('window/info');
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          경기 정보
        </li>
        <li
          className="ddList my-[5px]"
          onClick={() => {
            openMainWindow('window/news');
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          야구 뉴스
        </li>
        <li
          className="ddList my-[5px]"
          onClick={async () => {
            // @ts-ignore
            const check = await window.electron.getStoreValue('text');
            if (check) {
              openNewRealtime();
            } else {
              alert('현재 진행중인 야구경기가 없습니다.');
            }
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          실시간 중계
        </li>
        <li
          className="ddList my-[5px]"
          onClick={() => {
            openMainWindow('window/predictions');
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          승부 예측
        </li>
        {/* <li
          className="ddList my-[5px]"
          onClick={() => {}}
        >
          미니 게임
        </li> */}
        <li
          className="ddList my-[5px]"
          onClick={() => {
            openMainWindow('window/equipments');
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          캐릭터 꾸미기
        </li>
        <li
          className="ddList my-[5px]"
          onClick={() => {
            openMainWindow('window/setting');
            setMenumbar(false);
            setIgnoreMouseEvent(true);
          }}
        >
          설정
        </li>
        <li className="text-center my-[5px]">
          <ToggleButton2 />
        </li>
      </ul>
    </div>
  );
};

export default CustomDropdown;
