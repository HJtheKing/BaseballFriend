import { useRef, useEffect, useState } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import '../../css/GLBViewer.css';
import Model from './Model';
import CharacterDropView from './CharacterDropView';

interface GLBViewerProps {
  url: string;
}

function GLBViewer({ url }: GLBViewerProps) {
  const [dropView, setDropView] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);
  const dropdownRef = useRef<HTMLUListElement>(null);

  const handleModelClick = () => {
    setDropView(true);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      // containerRef가 아닌 dropdownRef를 사용하여 드롭다운 외부 클릭 감지
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node) &&
        event.target instanceof Element &&
        !event.target.closest('canvas')
      ) {
        setDropView(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, []);

  return (
    <div
      ref={containerRef}
      style={{
        width: '100%',
        height: '100%',
        position: 'relative',
        background: 'transparent',
      }}
      onClick={() => setDropView(false)}
    >
      <Canvas
        style={{ background: 'transparent' }}
        camera={{ position: [-13, 1, 1.2] }}
        gl={{ alpha: true }}
      >
        <ambientLight intensity={2} />
        <spotLight
          position={[10, 10, 10]}
          angle={0.15}
          penumbra={1}
        />
        <pointLight position={[-10, -10, -10]} />
        <Model
          url={url}
          onModelClick={handleModelClick}
        />
        <OrbitControls
        // enablePan={false}
        // enableZoom={false}
        />
      </Canvas>

      <CharacterDropView
        dropDown={dropView}
        ref={dropdownRef}
      />
    </div>
  );
}

export default GLBViewer;
