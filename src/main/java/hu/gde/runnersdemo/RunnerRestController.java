package hu.gde.runnersdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/runner")
public class RunnerRestController {

    @Autowired
    private LapTimeRepository lapTimeRepository;
    private RunnerRepository runnerRepository;
    private SponsorRepository sponsorRepository;

    @Autowired
    public RunnerRestController(RunnerRepository runnerRepository, LapTimeRepository lapTimeRepository,SponsorRepository sponsorRepository) {
        this.runnerRepository = runnerRepository;
        this.lapTimeRepository = lapTimeRepository;
        this.sponsorRepository=sponsorRepository;
    }

    @GetMapping("/{id}")
    public RunnerEntity getRunner(@PathVariable Long id) {
        return runnerRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/averagelaptime")
    public double getAverageLaptime(@PathVariable Long id) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            List<LapTimeEntity> laptimes = runner.getLaptimes();
            int totalTime = 0;
            for (LapTimeEntity laptime : laptimes) {
                totalTime += laptime.getTimeSeconds();
            }
            double averageLaptime = (double) totalTime / laptimes.size();
            return averageLaptime;
        } else {
            return -1.0;
        }
    }

    @GetMapping("")
    public List<RunnerEntity> getAllRunners() {
        return runnerRepository.findAll();
    }

    @PostMapping("/{id}/addlaptime")
    public ResponseEntity addLaptime(@PathVariable Long id, @RequestBody LapTimeRequest lapTimeRequest) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        if (runner != null) {
            LapTimeEntity lapTime = new LapTimeEntity();
            lapTime.setTimeSeconds(lapTimeRequest.getLapTimeSeconds());
            lapTime.setLapNumber(runner.getLaptimes().size() + 1);
            lapTime.setRunner(runner);
            lapTimeRepository.save(lapTime);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Runner with ID " + id + " not found");
        }


    }

    @GetMapping("/maxheight")
    public String getHighest() {
        List<RunnerEntity> runners = runnerRepository.findAll();
        if (runners.size() != 0) {
            int maxHeight = 0;
            RunnerEntity maxHeightRunner=null;

            for (RunnerEntity runner : runners) {

                if (maxHeight<runner.getHeight()){

                    maxHeightRunner=runner;
                }



            }
            return maxHeightRunner.getRunnerName();
        } else {
            return "null";
        }


    }
    @GetMapping("/averageheight")
    public double getAverageHeight() {
        List<RunnerEntity> runners = runnerRepository.findAll();
        if (runners.size() != 0) {
            int totalHeight = 0;
            int counnter = 0;
            for (RunnerEntity runner : runners) {
                totalHeight += runner.getHeight();
                counnter++;


            }
            return (double) totalHeight / counnter;
        } else {
            return -1.0;
        }
    }
    @PostMapping("/{id}/setsponsor")
    public ResponseEntity setSponsor(@PathVariable Long id, @RequestBody sponsorRequest sponsorRequest) {
        RunnerEntity runner = runnerRepository.findById(id).orElse(null);
        SponsorEntity sponsor = sponsorRepository.findById(sponsorRequest.getSponsorId()).orElse(null);
        if(runner != null && sponsor != null) {
            runner.setSponsor(sponsor);
            runnerRepository.save(runner);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Runner with ID " + id + " not found");
        }
    }

    public static class LapTimeRequest {
        private int lapTimeSeconds;

        public int getLapTimeSeconds() {
            return lapTimeSeconds;
        }

        public void setLapTimeSeconds(int lapTimeSeconds) {
            this.lapTimeSeconds = lapTimeSeconds;
        }
    }
    public static class sponsorRequest {
        private long sponsorId;

        public long getSponsorId() {
            return sponsorId;
        }

        public void setSponsorId(long sponsorId) {
            this.sponsorId = sponsorId;
        }
    }
}
