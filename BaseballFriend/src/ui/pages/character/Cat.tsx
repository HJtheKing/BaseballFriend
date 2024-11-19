import GLBViewer from './GLBViewer';

const Cat = () => {
  return (
    <div
      className="character"
      style={{
        width: '100vw',
        height: '100vh',
        background: 'transparent',
      }}
    >
      <GLBViewer url={'animation_class.glb'} />
    </div>
  );
};

export default Cat;
