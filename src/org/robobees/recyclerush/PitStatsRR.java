package org.robobees.recyclerush;

import java.util.Map;

import org.frc836.database.PitStats;

public class PitStatsRR extends PitStats {
	public boolean push_tote;
	public boolean push_bin;
	public boolean lift_tote;
	public boolean lift_bin;
	public boolean push_litter;
	public boolean load_litter;
	public short stack_tote_height;
	public short stack_bin_height;
	public short coop_totes;
	public short coop_stack_height;
	public boolean move_auto;
	public short auto_bin_score;
	public short auto_tote_score;
	public short auto_tote_stack_height;
	public short auto_step_bins;
	public String manipulation_description;
	public boolean need_upright_tote;
	public boolean need_upright_bin;
	public boolean can_upright_tote;
	public boolean can_upright_bin;
	
	public PitStatsRR() {
		init();
	}
	
	public void init() {
		super.init();
		push_tote = false;
		push_bin = false;
		lift_tote = false;
		lift_bin = false;
		push_litter = false;
		load_litter = false;
		stack_tote_height = 0;
		stack_bin_height = 0;
		coop_totes = 0;
		coop_stack_height = 0;
		move_auto = false;
		auto_bin_score = 0;
		auto_tote_score = 0;
		auto_tote_stack_height = 0;
		auto_step_bins = 0;
		manipulation_description = "";
		need_upright_tote = false;
		need_upright_bin = false;
		can_upright_tote = false;
		can_upright_bin = false;
	}
	
	
}
