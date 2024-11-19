import * as THREE from 'three';
import React, { useEffect, useState } from 'react';
import { useGraph } from '@react-three/fiber';
import { useGLTF } from '@react-three/drei';
import { GLTF, SkeletonUtils } from 'three-stdlib';

type GLTFResult = GLTF & {
  nodes: {
    Scene002: THREE.SkinnedMesh;
    Scene002_1: THREE.SkinnedMesh;
    spine_01: THREE.Bone;
    tail01: THREE.Bone;
    thigh_l: THREE.Bone;
    thigh_r: THREE.Bone;
    neutral_bone: THREE.Bone;
  };
  materials: {
    ['M_Chibi_Bear_15.002']: THREE.MeshStandardMaterial;
    ['M_Chibi_Emo_25.002']: THREE.MeshStandardMaterial;
  };
  // animations: GLTFAction[];
};

export function Bear15(props: JSX.IntrinsicElements['group']) {
  const [glbPath, setPath] = useState<string>('');
  const getAppPath = async (): Promise<string> => {
    // @ts-ignore
    return await window.electron.getAppPath();
  };
  useEffect(() => {
    const fetchPath = async () => {
      // @ts-ignore
      const appPath = await getAppPath();
      console.log('appPath == ', appPath);
      setPath(appPath);
    };

    fetchPath();
  }, []);

  const { scene } = useGLTF(`${glbPath}/bear15.glb`);
  const clone = React.useMemo(() => SkeletonUtils.clone(scene), [scene]);
  const { nodes, materials } = useGraph(clone) as GLTFResult;

  Object.values(materials).forEach((material) => {
    material.side = THREE.FrontSide;
  });

  return (
    <group
      {...props}
      dispose={null}
    >
      <group
        rotation={[Math.PI / 2, 0, 0]}
        scale={0.01}
      >
        <group
          onClick={(event) => {
            console.log(event);
          }}
          position={[0, -0.646, -35.905]}
          rotation={[1.537, 0, -Math.PI / 2]}
        >
          <primitive object={nodes.spine_01} />
          <primitive object={nodes.tail01} />
          <primitive object={nodes.thigh_l} />
          <primitive object={nodes.thigh_r} />
          <primitive object={nodes.neutral_bone} />
          <skinnedMesh
            geometry={nodes.Scene002.geometry}
            material={materials['M_Chibi_Bear_15.002']}
            skeleton={nodes.Scene002.skeleton}
          />
          <skinnedMesh
            geometry={nodes.Scene002_1.geometry}
            material={materials['M_Chibi_Emo_25.002']}
            skeleton={nodes.Scene002_1.skeleton}
          />
        </group>
      </group>
    </group>
  );
}

useGLTF.preload('/bear15.glb');
