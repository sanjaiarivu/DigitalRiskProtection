# Plagiarism and Source-Attribution Report

**Project:** BrandShield AI / DigitalRiskProtection  
**Review date:** 2026-10-12  
**Scope:** Authored project files only

## Executive conclusion

No confirmed external-source plagiarism was identified from the repository-local review.
The result is **inconclusive rather than a guarantee of originality**: the project has no
Git history in the supplied workspace, and public code-search services were unavailable
from the review environment.

The authored implementation has a coherent project-specific vocabulary (`BrandShield`,
`TechVault`, `AiExplanationOrchestrator`, and the detection signal model), a synthetic
demo dataset, and consistent frontend/backend naming. These are indicators of a
purpose-built implementation, but they are not proof of authorship.

## Scope and method

- Reviewed **104 authored files** under `frontend/src` and `backend/src`, plus the root
  README and dependency manifests.
- Reviewed approximately **10,860 physical source lines**:
  - 73 Java files
  - 27 TypeScript/TSX files
  - 4 YAML/SQL files
- Excluded `frontend/node_modules`, `frontend/dist`, `backend/target`, and
  `backend/.tools`. These are generated or vendored third-party artifacts and would
  produce misleading plagiarism results.
- Searched authored files for URLs, copyright/license notices, source-attribution
  language, “copied/adapted/based on” markers, and common placeholder text.
- Compared repeated five-line blocks and cross-file token similarity within the
  authored source.
- Attempted public exact-phrase/code searches for unique project identifiers. Those
  requests failed due to network/service transport errors, so no external match can be
  confirmed or ruled out from those services.

## Findings

| ID | Finding | Assessment | Confidence |
|---|---|---|---|
| F-01 | No explicit copied-source attribution or external repository reference was found in authored source. | No local plagiarism indicator | Medium |
| F-02 | The implementation uses project-specific names and domain logic, including the explainable detection engine and weighted risk signals documented in the README. | Supports original integration/design | Medium |
| F-03 | The demo data is explicitly synthetic and labeled as such; the `TechVault` records are internally consistent across the backend and UI. | Not plagiarism evidence | High |
| F-04 | Repeated source blocks occur mainly in Java DTO/entity accessors and standard React page structure. | Normal framework/application boilerplate; not a confirmed copied block | High |
| F-05 | The source contains standard framework/API idioms such as Spring controllers, JPA entities, React Router, React Query, Axios, and Levenshtein/Jaccard similarity. | Generic/common patterns; not attributable to a particular source | High |
| F-06 | Public-source verification could not be completed because external search requests timed out or failed at the transport layer. | Residual uncertainty | High |

## Notable evidence

- The README identifies the application as a hackathon edition and documents its
  detection methodology and limitations: [README.md](./README.md).
- The similarity algorithm is implemented as a multi-signal component with
  Levenshtein distance, token similarity, look-alike substitutions, and affix
  detection: [TextSimilarityAnalyzer.java](./backend/src/main/java/com/brandshield/detection/TextSimilarityAnalyzer.java).
- Demo records are explicitly described as synthetic and include purpose-built
  official, suspicious, and unrelated candidates: [DemoDataService.java](./backend/src/main/java/com/brandshield/service/DemoDataService.java).
- The project itself documents that authentication is disabled for the prototype;
  this is a product limitation, not a plagiarism signal:
  [SecurityConfig.java](./backend/src/main/java/com/brandshield/config/SecurityConfig.java).
- Third-party dependencies are declared in [pom.xml](./backend/pom.xml) and
  [package.json](./frontend/package.json). Their installed source and license files
  were excluded from the authorship assessment.

## Internal similarity assessment

Some high-similarity pairs were found among response DTOs and related Java model
classes. They share predictable getters/setters, constructors, field mapping, and
serialization annotations. Similarity among these files is expected because they
implement the same application contract and does not by itself indicate copying from
an external project.

No substantial identical multi-file implementation block was established across
unrelated authored modules. Exact URLs such as `localhost`, `techvault.com`, Twitter,
Google Play, and OpenAI endpoints are configuration or synthetic demo data, not
evidence of copied source code.

## Limitations and recommended verification

This report is not a legal determination and cannot identify private, unindexed, or
non-code sources. For a formal academic or compliance submission:

1. Obtain the author's Git history or earlier drafts and compare commit chronology.
2. Run the authored files through an approved source-code similarity service
   (e.g. MOSS/JPlag or the institution's licensed checker) against the required
   reference corpus.
3. Include dependency licenses separately; do not count package-manager contents as
   project plagiarism.
4. Preserve the exact checker version, corpus, exclusions, and threshold so the result
   is reproducible.

