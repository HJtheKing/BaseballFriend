import { useFrame, useGraph, useLoader } from '@react-three/fiber';
import { GLTFLoader, SkeletonUtils } from 'three-stdlib';
import * as THREE from 'three';
import { useEffect, useRef, useState } from 'react';
import AnimationController from '../../character/AnimationController';
import React from 'react';

const RealChr = ({ ...props }) => {
  const { scene: originalScene, animations } = useLoader(GLTFLoader, '0.glb');
  const clone = React.useMemo(
    () => SkeletonUtils.clone(originalScene),
    [originalScene],
  );
  const { nodes } = useGraph(clone);
  const catRef = useRef<THREE.Group>();
  const controllerRef = useRef<AnimationController>();
  const initialRotation = useRef<THREE.Euler>();
  const curBase = props.curBase;
  const curCnt = props.aniEvent;
  const [isRun, setisRun] = useState(false);

  useEffect(() => {
    for (const node in nodes) {
      const id = nodes[node].userData.objID;
      if (id >= 50) {
        if ((curBase === 0 && id === 700) || id === 111 || id === 403) {
          nodes[node].visible = true;
        } else {
          nodes[node].visible = false;
        }
      }
    }

    // clone을 사용하도록 변경
    for (let i = 0; i < clone.children.length; i++) {
      const child = clone.children[i];
      if (child.name.includes('Cat')) {
        catRef.current = child as THREE.Group;
        break;
      }
    }

    if (catRef.current) {
      initialRotation.current = catRef.current.rotation.clone();
    }
  }, []);

  useEffect(() => {
    let ani = 'a_idle';
    let animationTimeout: NodeJS.Timeout;

    if (catRef.current) {
      initialRotation.current = catRef.current.rotation.clone();

      controllerRef.current = new AnimationController(
        catRef.current,
        animations,
      );

      if (curCnt === 0) {
        if (curBase === 0) ani = 'e_realtime_ready';
      } else {
        if (curBase !== 0) {
          if (curCnt !== -2) {
            // 먼저 run 애니메이션 실행
            controllerRef.current.playAnimation('f_realtime_run', {
              duration: 0.016,
              loop: THREE.LoopRepeat,
            });
          } else {
            controllerRef.current.playAnimation('h_realtime_neg', {
              duration: 0.016,
              loop: THREE.LoopRepeat,
            });
            return;
          }
          setisRun(true);
          // 2초 후 다음 애니메이션으로 전환
          animationTimeout = setTimeout(() => {
            if (curCnt === 1 || curCnt === 2) {
              controllerRef.current?.playAnimation('g_realtime_pos', {
                duration: 0.016,
                loop: THREE.LoopOnce,
              });
            } else if (curCnt === -1) {
              controllerRef.current?.playAnimation('h_realtime_neg', {
                duration: 0.016,
                loop: THREE.LoopOnce,
              });
            }
            setisRun(false);
          }, 2000);

          return;
        } else {
          // curBase가 0인 경우의 기존 로직
          if (curCnt === 1 || curCnt === 2) ani = 'i_main_hit';
          else if (curCnt === -1) ani = 'j_main_hr';
        }
      }

      // curBase가 0이거나 curCnt가 0인 경우 바로 애니메이션 실행
      controllerRef.current.playAnimation(ani, {
        duration: 0.016,
        loop:
          ani === 'a_idle' || ani === 'e_realtime_ready'
            ? THREE.LoopRepeat
            : THREE.LoopOnce,
      });
    }

    return () => {
      if (controllerRef.current) {
        controllerRef.current.stopAnimation();
      }
      if (animationTimeout) {
        clearTimeout(animationTimeout);
      }
    };
  }, [clone, animations, curBase, curCnt]);

  useFrame((_, delta) => {
    if (isRun && catRef.current) {
      switch (curBase) {
        case 1:
          catRef.current.position.x -= 0.015 * (delta + 0.1);
          catRef.current.position.z += 0.5 * (delta + 0.1);
          break;
        case 2:
          catRef.current.position.x += 0.05 * (delta + 0.1);
          catRef.current.position.z += 0.5 * (delta + 0.1);
          break;
        case 3:
          catRef.current.position.x += 0.05 * (delta + 0.1);
          catRef.current.position.z -= 0.5 * (delta + 0.1);
          break;
        default:
          break;
      }
    }
  });

  return (
    <>
      {clone && (
        <primitive
          object={clone}
          position={props.position}
          rotation={props.rotation}
          scale={3}
        />
      )}
    </>
  );
};

export default RealChr;
