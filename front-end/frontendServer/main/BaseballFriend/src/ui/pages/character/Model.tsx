import { useRef, useEffect, useState } from 'react';
import { ThreeEvent, useLoader, useThree } from '@react-three/fiber';
import { GLTFLoader } from 'three-stdlib';
import '../../css/GLBViewer.css';
import * as THREE from 'three';

interface ModelProps {
  url: string;
  onModelClick: () => void;
}

export default function Model({ url, onModelClick }: ModelProps) {
  const gltf = useLoader(GLTFLoader, url);
  const [currentAnimation, setCurrentAnimation] = useState<
    'start' | 'idle' | 'swing' | 'out' | 'pitching'
  >('start');
  const modelRef = useRef<THREE.Group>(null);
  const { scene, animations } = gltf;
  const { gl, scene: threeScene, camera } = useThree();
  const mixerRef = useRef<THREE.AnimationMixer | null>(null);
  const actionRef = useRef<THREE.AnimationAction | null>(null);

  useEffect(() => {
    // 스페이스 바를 눌렀을 경우, 'idle'애니메이션일 때 스윙 애니메이션 재생
    const handleKeyPress = (event: KeyboardEvent) => {
      if (
        event.code === 'Space' &&
        (currentAnimation === 'idle' || currentAnimation === 'swing')
      ) {
        event.preventDefault();
        setCurrentAnimation('swing');
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [currentAnimation]);

  useEffect(() => {
    if (!animations.length) return;

    console.log(animations);
    const mixer = new THREE.AnimationMixer(scene);
    mixerRef.current = mixer;

    let animationIndex: number = 0;
    let duration: number = 0.016;

    // 애니메이션을 계속 실행하거나, 한번만 실행하게 함
    let loop: THREE.AnimationActionLoopStyles = THREE.LoopRepeat;

    switch (currentAnimation) {
      case 'start':
        animationIndex = 3; // 시작 애니메이션
        duration = 0.036;
        loop = THREE.LoopOnce;
        break;
      case 'swing':
        animationIndex = 4; // 스윙 애니메이션
        duration = 0.036;
        loop = THREE.LoopOnce;
        break;
      case 'idle':
        animationIndex = 0; // idle 애니메이션
        duration = 0.016;
        loop = THREE.LoopRepeat;
        break;
    }

    if (actionRef.current) {
      actionRef.current.stop();
    }

    const action = mixer.clipAction(animations[animationIndex]);
    action.setLoop(loop, Infinity);
    action.clampWhenFinished = true;
    action.play();
    actionRef.current = action;

    let animationFrameId: number;

    const animate = () => {
      mixer.update(duration);
      gl.render(threeScene, camera);
      animationFrameId = requestAnimationFrame(animate);
    };

    animate();

    const handleAnimationFinished = () => {
      if (currentAnimation === 'start') {
        setCurrentAnimation('idle');
      } else if (currentAnimation === 'swing') {
        setCurrentAnimation('idle');
      }
    };

    // 애니메이션 하나가 끝났을 경우 처리
    mixer.addEventListener('finished', handleAnimationFinished);

    return () => {
      cancelAnimationFrame(animationFrameId);
      mixer.removeEventListener('finished', handleAnimationFinished);
      mixer.stopAllAction();
    };
  }, [animations, scene, gl, threeScene, camera, currentAnimation]);

  const handleClick = (e: ThreeEvent<MouseEvent>) => {
    if (e) {
      // 오른쪽 클릭만 처리
      e.stopPropagation();
      onModelClick();
    }
  };

  return (
    <group
      ref={modelRef}
      position={[0.1, -0.3, 0.4]}
      onContextMenu={handleClick}
    >
      <primitive
        object={scene}
        scale={[1.5, 1.5, 1.5]}
      />
    </group>
  );
}
