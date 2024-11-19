// BaseballGame.tsx
import { useState, useRef, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import { Stadium } from '../realTime/Stadium';
import Model from '../../character/Model';
import * as THREE from 'three';

interface BallProps {
  position: THREE.Vector3;
}

function Ball({ position }: BallProps) {
  return (
    <mesh position={position}>
      <sphereGeometry args={[0.1, 32, 32]} />
      <meshStandardMaterial color="white" />
    </mesh>
  );
}

export default function BaseballGame() {
  const [gameState, setGameState] = useState<'waiting' | 'pitching' | 'swing' | 'result'>('waiting');
  const [score] = useState(0);
  const [ballPosition, setBallPosition] = useState(new THREE.Vector3(0, 1, 30));
  const animationFrameRef = useRef<number>();

  const throwBall = () => {
    setGameState('pitching');
    // 초기 위치 설정 (투수 마운드)
    setBallPosition(new THREE.Vector3(0, 1, 30));
  };

  useEffect(() => {
    let startTime: number;
    
    const animate = (currentTime: number) => {
      if (!startTime) startTime = currentTime;
      const elapsed = (currentTime - startTime) / 1000; // 초 단위로 변환

      if (gameState === 'pitching') {
        // 투구 궤적 계산
        const newZ = 30 - (elapsed * 15); // 초당 15 단위로 이동
        const newY = 1 + Math.sin(elapsed * Math.PI) * 0.5; // 포물선 움직임

        if (newZ <= 0) {
          setGameState('waiting');
          setBallPosition(new THREE.Vector3(0, 1, 30));
        } else {
          setBallPosition(new THREE.Vector3(0, newY, newZ));
        }
      }

      animationFrameRef.current = requestAnimationFrame(animate);
    };

    if (gameState === 'pitching') {
      startTime = 0;
      animationFrameRef.current = requestAnimationFrame(animate);
    }

    return () => {
      if (animationFrameRef.current) {
        cancelAnimationFrame(animationFrameRef.current);
      }
    };
  }, [gameState]);

  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <div style={{ position: 'absolute', padding: '20px', color: 'white' }}>
        Score: {score}
      </div>
      <Canvas
        camera={{ position: [0, 5, 10], fov: 60 }}
        shadows
      >
        <ambientLight intensity={0.5} />
        <directionalLight
          position={[10, 10, 5]}
          intensity={1}
          castShadow
        />
        {/* 타자 위치를 홈플레이트로 이동 */}
        <group position={[0, 0, 0]}>
          <Model
            url="animation_class.glb"
            onModelClick={() => {}}
          />
        </group>
        <Stadium />
        <Ball position={ballPosition} />
        <OrbitControls />
      </Canvas>
      <button
        style={{
          position: 'absolute',
          bottom: '20px',
          left: '50%',
          transform: 'translateX(-50%)',
          padding: '10px 20px',
          fontSize: '16px',
          backgroundColor: '#4CAF50',
          color: 'white',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer'
        }}
        onClick={throwBall}
      >
        Throw Ball
      </button>
    </div>
  );
}