// Stadium.tsx
import { useEffect, useRef } from 'react';
import * as THREE from 'three';
import { useLoader } from '@react-three/fiber';
import textureUrl from './grassTexture.png?url';
import { TextureLoader } from 'three';

export function Stadium() {
  const stadiumRef = useRef<THREE.Group>(null);
  // 야구장 크기 상수
  const FIELD_WIDTH = 150;
  const FIELD_LENGTH = 128;
  const INFIELD_SIZE = 45;
  const GREENINFIELD_SIZE = 37;

  const outfieldTexture = useLoader(TextureLoader, textureUrl);
  const infieldTexture = useLoader(TextureLoader, textureUrl);

  useEffect(() => {
    // 외야 텍스처 설정 (가로 방향 스트라이프)
    if (outfieldTexture) {
      outfieldTexture.wrapS = THREE.RepeatWrapping;
      outfieldTexture.wrapT = THREE.RepeatWrapping;
      outfieldTexture.repeat.set(1, 10); // x방향은 1번, y방향은 10번 반복
      outfieldTexture.colorSpace = THREE.SRGBColorSpace;
    }

    // 내야 텍스처 설정 (격자 패턴)
    if (infieldTexture) {
      infieldTexture.wrapS = THREE.RepeatWrapping;
      infieldTexture.wrapT = THREE.RepeatWrapping;
      infieldTexture.repeat.set(5, 5); // x, y 방향 모두 5번 반복
      infieldTexture.colorSpace = THREE.SRGBColorSpace;
    }
  }, [outfieldTexture, infieldTexture]);

  return (
    <group
      ref={stadiumRef}
      position={[0, -2, 0]}
    >
      {/* 외야 (녹색 잔디) */}
      <mesh
        rotation={[-Math.PI / 2, 0, 0]}
        receiveShadow
      >
        <planeGeometry args={[FIELD_WIDTH, FIELD_LENGTH, 100, 100]} />
        <meshStandardMaterial
          map={outfieldTexture}
          color="#2d5e1e"
          roughness={0.8}
          metalness={0.1}
          normalScale={new THREE.Vector2(0.5, 0.5)}
          bumpScale={0.5}
        />
      </mesh>

      {/* 내야 다이아몬드
      <mesh
        position={[0, 0.01, -20]}
        rotation={[-Math.PI / 2, 0, 0]}
        receiveShadow
      >
        <shapeGeometry args={[getInfieldShape(INFIELD_SIZE + 30)]} />
        <meshStandardMaterial
          color="#c17f59"
          roughness={1}
          metalness={0}
        />
      </mesh> */}

      {/* 내야 잔디 */}
      <mesh
        position={[0, 0.1, 16]}
        rotation={[-Math.PI / 2, 0, 0]}
        receiveShadow
      >
        <shapeGeometry args={[getInfieldShape(GREENINFIELD_SIZE - 1)]} />
        <meshStandardMaterial
          map={infieldTexture}
          color="#2d8e1e"
          roughness={0.8}
          metalness={0.5}
        />
      </mesh>

      {/* 외야 펜스 */}
      <group position={[0, 0, FIELD_LENGTH / 3 + 39]}>
        <mesh
          position={[0, 5, 0]}
          castShadow
        >
          <cylinderGeometry
            args={[FIELD_WIDTH / 1.5, FIELD_WIDTH / 1.3, 10, 32, 1, true]}
          />
          <meshStandardMaterial
            color="#2b2b2b"
            side={THREE.DoubleSide}
            // opacity={0.9}
          />
        </mesh>
      </group>

      {/* 외야 펜스 */}
      <group position={[0, 20, FIELD_LENGTH / 3 + 39]}>
        <mesh
          position={[0, 0, -25]}
          castShadow
        >
          <boxGeometry args={[5, 5, 0]} />
          <meshStandardMaterial
            color="#2b2b2b"
            side={THREE.DoubleSide}
            // opacity={0.9}
          />
        </mesh>
      </group>

      {/* 베이스라인 */}
      <group position={[0, 0.02, 34.5 - GREENINFIELD_SIZE]}>
        {getBaseLines(GREENINFIELD_SIZE + 40)}
      </group>

      {/* 베이스 */}
      <group position={[0, 0.03, 14.5]}>{getBases(GREENINFIELD_SIZE)}</group>

      {/* 투수 마운드 */}
      <mesh
        position={[0, 0.2, INFIELD_SIZE / 2 + 11]}
        receiveShadow
      >
        <cylinderGeometry args={[2, 3, 0.6, 32]} />
        <meshStandardMaterial color="#a06b3d" />
      </mesh>
    </group>
  );
}

// 내야 다이아몬드 모양 생성 함수
function getInfieldShape(size: number) {
  const shape = new THREE.Shape();

  // 다이아몬드 모양 그리기
  shape.moveTo(0, 0); // 홈 플레이트
  shape.lineTo(-size / 2, -size / 2); // 1루
  shape.lineTo(0, -size); // 2루
  shape.lineTo(size / 2, -size / 2); // 3루
  shape.lineTo(0, 0); // 다시 홈으로

  return shape;
}
// 베이스라인 생성
function getBaseLines(size: number) {
  return (
    <>
      {/* <mesh>
        <boxGeometry args={[0.2, 0.01, size * 0.71]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh> */}
      <mesh
        position={[(size / 2) * 0.71, 0, (size / 2) * 0.71]}
        rotation={[0, -Math.PI / 4, 0]}
      >
        <boxGeometry args={[0.2, 0.2, size]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[(-size / 2) * 0.71, 0, (size / 2) * 0.71]}
        rotation={[0, Math.PI / 4, 0]}
      >
        <boxGeometry args={[0.2, 0.2, size]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
    </>
  );
}

// 베이스 생성
function getBases(size: number) {
  return (
    <>
      {/* 2루 */}
      <mesh rotation={[0, -Math.PI / 4, 0]}>
        <boxGeometry args={[1.5, 0.2, 1.5]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[0, 0, 2]}
        receiveShadow
      >
        <cylinderGeometry args={[2, 2, 0.15]} />
        <meshStandardMaterial color="#c17f59" />
      </mesh>
      {/* 1루 */}
      <mesh
        position={[size / 2, 0, size / 2]}
        rotation={[0, -Math.PI / 4, 0]}
      >
        <boxGeometry args={[1, 0.2, 1]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[size / 2, 0, size / 2]}
        receiveShadow
      >
        <cylinderGeometry args={[2, 2, 0.15]} />
        <meshStandardMaterial color="#c17f59" />
      </mesh>
      {/* 홈 */}
      <mesh
        position={[0, 0.2, size + 0.5]}
        rotation={[0, -Math.PI / 4, 0]}
      >
        <boxGeometry args={[0.9, 0.15, 1]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[0, 0.2, size]}
        rotation={[0, 0, 0]}
      >
        <boxGeometry args={[1.35, 0.15, 1]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[0, 0, size / 2 + 19]}
        receiveShadow
      >
        <cylinderGeometry args={[4, 4, 0.2]} />
        <meshStandardMaterial color="#c17f59" />
      </mesh>
      <mesh
        position={[0, 0.02, size / 2 + 23]}
        receiveShadow
      >
        <cylinderGeometry
          args={[2, 55, 0.1, 32, 1, false, 2.35, Math.PI / 2]}
        />
        <meshStandardMaterial color="#c17f59" />
      </mesh>
      {/* 3루 */}
      <mesh
        position={[-size / 2, 0, size / 2]}
        rotation={[0, -Math.PI / 4, 0]}
      >
        <boxGeometry args={[1, 0.2, 1]} />
        <meshStandardMaterial color="#ffffff" />
      </mesh>
      <mesh
        position={[-size / 2, 0, size / 2]}
        receiveShadow
      >
        <cylinderGeometry args={[2, 2, 0.15]} />
        <meshStandardMaterial color="#c17f59" />
      </mesh>
    </>
  );
}
