import { useRef, useEffect } from 'react';
import { useLoader } from '@react-three/fiber';
import { GLTFLoader } from 'three-stdlib';
import * as THREE from 'three';

interface ModelProps {
  url: string;
  head: number;
  body: number;
  arm: number;
  background: number;
}

export default function EquipModel({
  url,
  head,
  body,
  arm,
  background,
}: ModelProps) {
  const gltf = useLoader(GLTFLoader, url);
  const modelRef = useRef<THREE.Group>(null);
  const { scene, animations } = gltf;
  const gltfObj = gltf.nodes;

  useEffect(() => {
    console.log(gltfObj, animations);
    for (const i in gltfObj) {
      const data = gltfObj[i];
      const objID = data.userData.objID;
      if (
        objID === head ||
        objID === body ||
        objID === arm ||
        objID === background
      ) {
        if (data.children) {
          data.children.forEach((child) => {
            child.visible = true;
          });
          data.visible = true;
        }
      } else if (data.userData.objID >= 50 && !i.includes('Scene')) {
        data.visible = false;
      }
    }
  }, [arm, body, gltfObj, head]);

  return (
    <group
      ref={modelRef}
      position={[0, -5, 0]}
    >
      <primitive
        object={scene}
        scale={[6, 6, 6]}
      />
    </group>
  );
}
