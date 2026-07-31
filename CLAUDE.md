# CLAUDE.md

Behavioral guidelines to reduce common LLM coding mistakes. Merge with project-specific instructions as needed.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

## 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:
- Always enter Plan Mode first.
- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

## 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

## 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:
- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:
- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

## 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:
- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:
```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

Strong success criteria let you loop independently. Weak criteria ("make it work") require constant clarification.

---

**These guidelines are working if:** fewer unnecessary changes in diffs, fewer rewrites due to overcomplication, and clarifying questions come before implementation rather than after mistakes.

---

## 프로젝트 개요

**공연 예매 플랫폼** — Spring(Boot) + React. 공연(Performance) 도메인 필요.

궁극적인 목표는 **대규모 트래픽을 처리하는 예매 플로우** 구축이며, 다음 단계로 설계한다:

1. **대기열** — Redis Sorted Set으로 구현.
2. **대기 순번 확인** — 프론트엔드는 폴링(polling)으로 현재 대기 순번을 조회.
3. **입장 스케줄러** — 현재 임계 TPS를 고려해 대기열에서 사용자를 순차 입장시킴. Spring `@Scheduled`(고정 주기) + **ShedLock**(Redis 기반 분산 락)을 사용한다. 서버가 스케일아웃돼도 인스턴스 간 중복 실행을 막기 위해 Redis 락으로 단일 실행을 보장하며, Quartz처럼 DB(JobStore)에 의존하는 방식은 사용하지 않는다.
4. **좌석 선점** — Redis 원자적 연산(Lua Script)으로 동시성 제어 보장.
5. **예매 확정 처리** — DB에 즉시 쓰지 않고 메시지 큐(MQ)로 요청을 한 번 분산.
6. **최종 커밋** — MQ를 거친 요청을 최종적으로 DB 트랜잭션으로 커밋.

이 플로우와 관련된 작업(대기열, 좌석 선점, 예매 확정 등)을 할 때는 위 순서와 각 단계의 기술 선택(Redis Sorted Set, Lua Script, MQ 분산 처리)을 전제로 설계/구현한다.