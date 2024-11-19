const CharacterDropView = ({ ...props }) => {
  const curD = props.dropDown;
  const dropdownRef = props.ref;

  const handleRealtimeClick = async () => {
    // @ts-ignore
    const check = await window.electron.getStoreValue('texts');
    if (check) {
      // @ts-ignore
      window.electron.openNewRealtime();
    } else {
      alert('시합중이지 않습니다.');
    }
  };

  return (
    <>
      {curD && (
        <ul
          ref={dropdownRef}
          style={{
            background: '#D4DFE9',
            boxShadow: '4px 4px 4px rgba(0, 0, 0, 0.25)',
            borderRadius: '5px',
            width: '50%',
            height: '10%',
            position: 'absolute',
            bottom: '65%',
            right: '23%',
            padding: '0.5rem',
            listStyle: 'none',
            margin: 0,
            minWidth: '100px',
            zIndex: 1000,
          }}
        >
          <li className="ddList">경기 정보</li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
          >
            야구 뉴스
          </li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
            onClick={handleRealtimeClick}
          >
            실시간 중계
          </li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
          >
            승부 예측
          </li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
          >
            미니 게임
          </li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
          >
            캐릭터 꾸미기
          </li>
          <li
            className="ddList"
            style={{ marginTop: '5px' }}
          >
            마이 페이지
          </li>
        </ul>
      )}
    </>
  );
};

export default CharacterDropView;
